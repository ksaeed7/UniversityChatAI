import json
import boto3
import os
import openai
from botocore.exceptions import ClientError

# Initialize DynamoDB
dynamodb = boto3.resource('dynamodb')
# Ensure you replace 'YourDynamoDBTableName' with the actual name of your DynamoDB table
table = dynamodb.Table(os.environ.get('DYNAMODB_TABLE_NAME', 'YourDynamoDBTableName'))

# Set your OpenAI API key from an environment variable for security reasons
openai.api_key = os.environ.get('OPENAI_API_KEY')

def lambda_handler(event, context):
    for record in event['RECORDNAME']:
        # Proceed only for 'INSERT' events indicating new items added to the table
        if record['eventName'] == 'INSERT':
            new_image = record['dynamodb']['NewImage']
            # Check if the message is from the chatbot, adjust 'sender' and 'messageID' as per your table's schema
            if new_image['sender']['S'].lower() == 'chatbot': # can be any identifier here.
                message_id = new_image['messageID']['S']
                content = new_image['content']['S']
                
                try:
                    # Call GPT-3 to generate a response based on the message content
                    response = openai.Completion.create(
                        engine="YOUR_FINE_TUNED_MODEL",  # Or any other suitable model
                        prompt=content,
                        max_tokens=150,
                        temperature=0.7
                    )

                    # Extract response text
                    response_text = response.get('choices', [{}])[0].get('text', '').strip()

                    # Update the DynamoDB table item with the generated response
                    table.update_item(
                        Key={'messageID': message_id},  # Adjust based on your table's primary key attribute
                        UpdateExpression='SET content = :response',
                        ExpressionAttributeValues={':response': response_text}
                    )
                except openai.Error as e:
                    print(f"OpenAI error: {str(e)}")
                except ClientError as e:
                    print(f"DynamoDB client error: {str(e)}")
                except Exception as e:
                    print(f"Unexpected error: {str(e)}")

    return {
        'statusCode': 200,
        'body': json.dumps('Lambda function execution completed successfully.')
    }
