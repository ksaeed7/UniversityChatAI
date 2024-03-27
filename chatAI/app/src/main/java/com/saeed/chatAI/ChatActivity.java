package com.example.chatbottest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.DataStoreItemChange;
import com.amplifyframework.datastore.generated.model.Conversation;
import com.amplifyframework.datastore.generated.model.Message;
import com.amplifyframework.datastore.generated.model.User;
import com.example.chatbottest.adapter.ChatAdapter;
import com.example.chatbottest.ConvoListActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class ChatActivity extends AppCompatActivity {

    private ChatAdapter chatAdapter;
    private ArrayList<Message> messageArrayList = new ArrayList<>();
    ArrayList<String> convoIDList = new ArrayList<>();
    private String convoId="";
    private boolean isQueried = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode ==KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent(getApplicationContext(), ConvoListActivity.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode,event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //set conversation Initialization between for testing purposes
        /*
        * type Conversation
            @model
            {
                id: ID!
                name: String!
                recipient: String!
                sender: String!
            }
        * */
        boolean doOnce = true;
        //set adapter
        ListView messageListView = findViewById(R.id.messageListView);

        chatAdapter = new ChatAdapter(getApplicationContext(), R.id.messageListView, messageArrayList );

        messageListView.setAdapter(chatAdapter);
        convoId = getConvoId();
        chatAdapter.otheruser = getAllUsers();
            String currentUser = Amplify.Auth.getCurrentUser().getUsername();
            //Conversation conversation = Conversation.builder().name("Self Send").recipient(currentUser).sender(currentUser).build();
            /*Amplify.DataStore.save(
                    conversation,
                    target -> {
                        Log.i("Amplify Datastore", "Stored Conversation Successfully");
                    },
                    onError -> {
                        Log.e("Amplify Datastore", onError.getMessage());
                    }
            );*/
        //convoId = conversation.getId().toString();

        isQueried = false;

        /*Amplify.DataStore.query(
                Conversation.class,
                Where.matches(Conversation.NAME.contains("Self Send")),
                (conversations) -> runOnUiThread( () -> {
                            while(conversations.hasNext()){
                                Conversation next = conversations.next();
                                convoIDList.add(next.getId().toString());
                                Log.i("QUERYING THIS BITCH", next.getId().toString());
                            }
                            setQueried();
                            getPreviousMessage();
                        }
                ),
                Throwable::printStackTrace
        );*/
        //convoId = convoIDList.get(0);





        //get message previous


        getPreviousMessage();
        //receive new messages

        Amplify.DataStore.observe(
                Message.class,
                cancelabe -> Log.i("Amplify-observer", "Observation has began"),
                this::onNewMessageReceived,
                failure -> Log.e("MyAmplifyObserverError", "Observation failed", failure),
                () -> Log.i("Amplify-observer", "Observation is completed successfully")
        );
    }

    private <T extends Model> void onNewMessageReceived(DataStoreItemChange<Message> messagedChanged) {
        if(messagedChanged.type().equals(DataStoreItemChange.Type.CREATE))
        {
            Message message = messagedChanged.item();
            Log.e("Message received Convo ID", message.getConversationId());
            Log.e(" Convo ID", convoId);
            if(message.getConversationId().equals(convoId)) {
                Log.e(" Added", "Message ");
                messageArrayList.add(message);
                runOnUiThread(() -> chatAdapter.notifyDataSetChanged());
            }
        }

    }
    /*private void setQueried(){
        if(!convoIDList.isEmpty())
            convoId = convoIDList.get(0);
        isQueried = true;
    }*/
    private void getPreviousMessage() {
        if(convoId==""){
            Log.e("CONVOID", "IS NOT AVAILABLE");
        }
        else {
            Log.e("CONVOID", "IS AVAILABLE");
            Amplify.DataStore.query(
                    Message.class,
                    Where.matches(Message.CONVERSATION_ID.contains(convoId)).sorted(Message.DATE.ascending()),
                    (messages) -> runOnUiThread(() -> {
                                while (messages.hasNext()) {
                                    Message message = messages.next();
                                    messageArrayList.add(message);
                                    chatAdapter.notifyDataSetChanged();

                                }

                            }
                    ),
                    Throwable::printStackTrace

            );
        }
    }

    public void onClickSendMessage(View view) {



            EditText txtMessageContent = findViewById(R.id.txtMessageContent);
            String messageContent = txtMessageContent.getText().toString();
            AuthUser currentUser = Amplify.Auth.getCurrentUser();
            if (!messageContent.isEmpty()) {

                /*Message message = Message.builder().user(
                        User.justId(currentUser.getUserId())
                ).content(messageContent).conversationId(convoId).date(new Temporal.DateTime(new Date(), getTimeDiff())).build();*/

               /* Amplify.DataStore.start(
                        ()->Log.i("DATSTORE Start", "Claered success"),
                        error ->Log.e("Datastore Start", "Failed"));*/

               /* Message message1 = Message.builder().content(
                        messageContent
                ).conversationId(
                        convoId
                ).date(new Temporal.DateTime(new Date(), getTimeDiff()))
                        .user(
                        User.justId(currentUser.getUserId())

                ).build();*/
                //Message message1 = Message.builder().build();
                //Log.i("MESSAGE BUILT", message1.getConversationId().toString());
                Message message = Message.builder().content(
                        messageContent
                ).conversationId(
                        convoId
                ).messageSender(
                        currentUser.getUserId().toString()
                ).date(
                        new Temporal.DateTime(new Date(), getTimeDiff())
                ).build();
                Log.i("MESSAGE BUILT", convoId);
                Amplify.DataStore.save(message,
                        target -> {
                            Log.i("Amplify Datastore", "Message was sent successfully");
                        },
                        onError -> {
                            Log.e("Amplify Datastore", "failure", onError);
                        }
                );
                /*Amplify.DataStore.query(
                        Message.class,
                        Where.matches(Message.CONVERSATION_ID.contains(convoId)),
                        (messages) -> runOnUiThread( () -> {
                                    while(messages.hasNext()){
                                        Message next = messages.next();
                                        Amplify.DataStore.delete(
                                                next,
                                                onSuccess -> {
                                                    Log.e("Amplify Delete Success", "Successfully Deleted");
                                                },
                                                onError -> {
                                                    Log.e("Amplify Delete Error", onError.getMessage());
                                                }

                                        );
                                        //convoIDList.add(next.getId().toString());
                                        // Log.i("QUERYING THIS BITCH", next.getId().toString());
                                    }

                                }
                        ),
                        Throwable::printStackTrace
                );*/
            }
            txtMessageContent.setText("");

        /*ArrayList<String> convoIDList = new ArrayList<>();

        Amplify.DataStore.query(
                Conversation.class,
                Where.matches(Conversation.NAME.contains("Self Send")),
                (conversations) -> runOnUiThread( () -> {
                            while(conversations.hasNext()){
                                Conversation next = conversations.next();
                                convoIDList.add(next.getId().toString());
                                Log.i("QUERYING THIS BITCH", next.getId().toString());
                            }

                        }
                ),
                Throwable::printStackTrace
        );

        if(!convoIDList.isEmpty()) {


            Log.i("STORED SHIT FUCKING WORK", convoIDList.get(0));
        }
        else{
            Log.i("STORED SHIT FUCKING WORK", "BITCHES EMPTY");
        }*/

        /*String currentUser = Amplify.Auth.getCurrentUser().getUsername();
        Conversation conversation = Conversation.builder().name("Self Send").recipient(currentUser).sender(currentUser).build();
            Amplify.DataStore.save(
                    conversation,
                    target -> {
                        Log.i("Amplify Datastore", "Stored Conversation Successfully");
                    },
                    onError -> {
                        Log.e("Amplify Datastore", onError.getMessage());
                    }
            );

*/
    }

    private int getTimeDiff() {
        GregorianCalendar calendar = new GregorianCalendar();
        TimeZone timeZone = calendar.getTimeZone();
        int offset = timeZone.getRawOffset();
        return (int) TimeUnit.SECONDS.convert(offset,TimeUnit.MILLISECONDS);
    }
    private String getConvoId() {
        return getIntent().getStringExtra("conversationID");
    }
    private String getAllUsers() {
        String name = getIntent().getStringExtra("sender_name");
        return name;
    }
}


/* Deleting an item

Amplify.DataStore.query(
                Conversation.class,
                Where.matches(Conversation.NAME.contains("Self Send")),
                (conversations) -> runOnUiThread( () -> {
                            while(conversations.hasNext()){
                                Conversation next = conversations.next();
                                Amplify.DataStore.delete(
                                        next,k
                                        onSuccess -> {
                                            Log.e("Amplify Delete Success", "Successfully Deleted");
                                        },
                                        onError -> {
                                            Log.e("Amplify Delete Error", onError.getMessage());
                                        }

                                );
                               //convoIDList.add(next.getId().toString());
                               // Log.i("QUERYING THIS BITCH", next.getId().toString());
                            }

                        }
                ),
                Throwable::printStackTrace
        );
 */