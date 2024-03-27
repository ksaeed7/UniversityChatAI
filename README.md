# UniversityChatAI

## Description

 A chat application that has a chatbot embedded that is fine tuned specifically for students for ECE 270 at the University. You can chat with other users, chat with an AI course assistant, or even create groups. This project is an Android-based chat application that integrates AWS services through AWS Amplify for backend functionality, uses DynamoDB for data storage, Cognito for user authentication, and leverages OpenAI's GPT-3, including fine-tuning, for a chatbot tailored towards student needs for ECE 270.

## Android Environment Setup

### Prerequisites

- Android Studio
- Java JDK
- Android SDK

### Steps

1. **Install Android Studio**: Download from the [official website](https://developer.android.com/studio).
2. **Setup Java JDK**: Ensure Java JDK is installed and configured.
3. **Configure Android SDK**: Through the SDK Manager in Android Studio, install the necessary SDKs.
4. **Clone the Project**: Clone this repository to your machine.
5. **Open and Build the Project**: Open the project in Android Studio and build it to fetch all dependencies.

## AWS Environment Setup with Amplify

### Prerequisites

- An AWS account
- AWS CLI and Amplify CLI installed

### Steps

1. **Install Amplify CLI**: `npm install -g @aws-amplify/cli`.
2. **Configure Amplify**: `amplify configure` to setup Amplify CLI with your AWS account.
3. **Initialize Amplify**: In your project directory, `amplify init` to start Amplify, following the setup prompts.
4. **Add Authentication**: `amplify add auth` for Cognito User Pool setup.
5. **Add API and Database**: `amplify add api`, selecting DynamoDB and configuring your data models.
6. **Deploy Backend**: `amplify push` to create and configure the cloud resources.

## Client-Side Functionalities

- **User Authentication**: Including signup, login, and email verification using AWS Cognito.
- **Chat Interface**: Users can send and receive messages in real-time.
- **Chatbot Interaction**: Messages for the chatbot trigger a serverless Lambda function to generate responses using OpenAI's GPT-3.

## Serverless Lambda Function

`lambda_trigger.py` is activated by new messages in DynamoDB tagged as "chatbot":
- It processes `INSERT` events for new messages.
- Uses OpenAI GPT-3 to create responses based on the content.
- Updates DynamoDB with the chatbot response.

## Fine-Tuning OpenAI GPT-3

To enhance the chatbot's performance and make its responses more aligned with the application's context:

### Prerequisites

- Access to OpenAI API
- A dataset for training

### Steps

1. **Prepare Training Data**: Format your dataset according to OpenAI's specifications, including prompts and expected responses.
2. **Fine-Tune the Model**: Use the OpenAI API to train a new model instance with your dataset.
    ```
    openai api fine_tunes.create -t path_to_your_training_data.jsonl -m davinci
    ```
3. **Integration**: After training, specify the fine-tuned model when generating responses in the `lambda_trigger.py` script.
    ```python
    response = openai.Completion.create(
      engine="your_fine_tuned_model_id",
      prompt=content,
      max_tokens=150,
      temperature=0.7
    )
    ```

## AWS Services Used

- **Amplify**: Facilitates backend setup for auth, API, and database.
- **DynamoDB**: Stores users and chat messages.
- **Cognito**: Handles user sign-in and management.
- **Lambda**: Executes the serverless function for chatbot interactions.
- **IAM**: Manages access permissions for AWS resources.

## OpenAI Integration

- Integrates GPT-3 for generating chatbot responses, enhanced by custom fine-tuning to suit specific conversational contexts.

## Deployment

- **Android App**: Build the APK in Android Studio for distribution.
- **AWS Backend**: Deploy using the Amplify CLI.

## Contributing

Contributions are welcome. Please fork the repository, commit your changes, and submit a pull request.

## License

This project is licensed under the [MIT License](LICENSE.md).
