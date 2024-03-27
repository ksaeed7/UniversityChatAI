package com.example.chatbottest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.amplifyframework.auth.AuthChannelEventName;
import com.amplifyframework.auth.AuthException;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.DataStoreException;
import com.amplifyframework.datastore.DataStoreItemChange;
import com.amplifyframework.datastore.generated.model.Conversation;
import com.amplifyframework.datastore.generated.model.Message;
import com.amplifyframework.datastore.generated.model.User;
import com.amplifyframework.hub.HubChannel;
import com.example.chatbottest.adapter.ConversationAdapter;

import java.util.ArrayList;

public class ConvoListActivity extends AppCompatActivity {

    private static User current_user_app;
    private ConversationAdapter convoAdapter;

    private ArrayList<Conversation> convoArrayList = new ArrayList<>();
    private int adapterSelect = 0;
    //ListView userlistView;
    private ListView conversationListView;

    private ArrayAdapter<String> userArrayAdapter;
    private ArrayList<User> userArrayList = new ArrayList<>();
    private ArrayList<String> userArrayListName = new ArrayList<>();
    private int chatbotIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convo_list);
        getAllUsers();

        conversationListView = findViewById(R.id.convo_list_view);

        convoAdapter = new ConversationAdapter(getApplicationContext(), R.id.convo_list_view, convoArrayList);
        conversationListView.setAdapter(convoAdapter);
        setChatbotSelected();
        /*String userId = Amplify.Auth.getCurrentUser().getUserId();
        Amplify.DataStore.save(
                User.builder().name("Khalid Saeed").id(userId).build(),
                this::onSavedSuccess,
                this::onSaveError
        );*/

        //userlistView = findViewById(R.id.convo_list_view);
        userArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, userArrayListName);


        Intent intent = new Intent(this, ChatActivity.class);
        Amplify.DataStore.observe(
                Conversation.class,
                cancelabe -> Log.e("Amplify-observer-Conversation", "Observation has began***"),
                this::onNewConvoReceived,
                failure -> Log.e("MyAmplifyObserverConvoError", "Observation failed", failure),
                () -> Log.i("Amplify-observer-conversation", "Observation is completed successfully")
        );
        Amplify.DataStore.observe(
                User.class,
                cancelabe -> Log.e("Amplify-observer-user", "Observation has began***"),
                this::onNewUserReceived,
                failure -> Log.e("MyAmplifyObserverError", "Observation failed", failure),
                () -> Log.i("Amplify-observer-user", "Observation is completed successfully")
        );
        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //check if in selecting users screen or conversations
                //if in user screen check that conversation doesnt exist by checking if there is a conversation between the
                if (adapterSelect == 0) {
                    String sender = "";
                    Conversation selectedItem = (Conversation) adapterView.getItemAtPosition(i);
                    String conversationSendToIntent = selectedItem.getId();
                    if (selectedItem.getRecipient().equals(current_user_app.getName())) {
                        sender = selectedItem.getSender();
                    } else {
                        sender = selectedItem.getRecipient();
                    }
                    Log.e("Conversation Select", conversationSendToIntent);
                    intent.putExtra("conversationID", conversationSendToIntent);
                    intent.putExtra("sender_name", sender);
                    startActivity(intent);
                } else if (adapterSelect == 1) {
                    //check if user id == curr user.id thats the name of current user.
                    //
                    String sender = "";
                    boolean addConvo = true;
                    String convoItemId = "";
                    String selectedString = (String) adapterView.getItemAtPosition(i);
                    for (Conversation convo_item : convoArrayList
                    ) {
                        if ((convo_item.getRecipient().equals(selectedString) && convo_item.getSender().equals(current_user_app.getName())) ||
                                (convo_item.getSender().equals(selectedString) && convo_item.getRecipient().equals(current_user_app.getName()))) {
                            addConvo = false;
                            convoItemId = convo_item.getId();
                            if (convo_item.getRecipient().equals(current_user_app.getName())) {
                                sender = convo_item.getSender();
                            } else {
                                sender = convo_item.getRecipient();
                            }
                        }
                    }
                    Log.e("Sender", sender);
                    if (addConvo == true) {
                        Conversation conversation = Conversation.builder().name(selectedString + "_" + current_user_app.getName()).recipient(selectedString).sender(current_user_app.getName()).build();
                        Amplify.DataStore.save(
                                conversation,
                                target -> {
                                    Log.i("Amplify Datastore", "Stored Conversation Successfully");
                                },
                                onError -> {
                                    Log.e("Amplify Datastore", onError.getMessage());
                                }
                        );

                        String conversationSendToIntent = conversation.getId();
                        Log.e("ConversationID New", conversationSendToIntent);
                        intent.putExtra("conversationID", conversationSendToIntent);
                        intent.putExtra("sender_name", sender);
                        Log.e("Conversation Select", conversationSendToIntent);
                        startActivity(intent);

                    } else {
                        String conversationSendToIntent = convoItemId;
                        Log.e("Conversation Select", conversationSendToIntent);
                        intent.putExtra("conversationID", conversationSendToIntent);
                        intent.putExtra("sender_name", sender);
                        startActivity(intent);


                    }
                    adapterSelect = 0;
                }
            }
        });

    }

    private <T extends Model> void onNewUserReceived(DataStoreItemChange<User> userChanged) {
        if (userChanged.type().equals(DataStoreItemChange.Type.CREATE)) {
            User user = userChanged.item();

            userArrayList.add(user);
            runOnUiThread(() -> userArrayAdapter.notifyDataSetChanged());

        }
    }

    private <T extends Model> void onNewConvoReceived(DataStoreItemChange<Conversation> convoChanged) {
        if (convoChanged.type().equals(DataStoreItemChange.Type.CREATE)) {
            Conversation conversation = convoChanged.item();

            convoArrayList.add(conversation);
            runOnUiThread(() -> convoAdapter.notifyDataSetChanged());
            setChatbotSelected();

        }

    }

    private void onSaveError(DataStoreException e) {
        Log.e("Save", e.getMessage());
    }

    private <T extends Model> void onSavedSuccess(DataStoreItemChange<T> tDataStoreItemChange) {
        Log.e("Save", "Worked");
    }

    private void getAllUsers() {
        Log.e("Getting ALL UserNames", "Attmept");
        Amplify.DataStore.query(
                User.class,
                Where.matchesAll(),
                (users) -> runOnUiThread(() -> {

                    while (users.hasNext()) {
                        User user = users.next();
                        Log.e("Getting ALL UserID", user.getId());
                        userArrayList.add(user);
                        userArrayListName.add(user.getName());
                    }
                    String currentUser = Amplify.Auth.getCurrentUser().getUserId();
                    Log.e("Getting ALL UserID", currentUser);
                    for (User userItems : userArrayList
                    ) {
                        if (userItems.getId().equals(currentUser)) {
                            Log.e("CurrentUser", "IS equal");
                            current_user_app = userItems;
                        }
                    }
                    if (!userArrayList.isEmpty()) {
                        userArrayAdapter.notifyDataSetChanged();
                        getAllConversations();
                    }
                }),
                Throwable::printStackTrace
        );

    }

    private void getAllConversations() {
        String currentUserName = current_user_app.getName();

        Amplify.DataStore.query(
                Conversation.class,
                Where.matchesAll(),
                (conversations) -> runOnUiThread(() -> {
                    while (conversations.hasNext()) {
                        Conversation convo = conversations.next();
                        Log.e("Getting ALL CONVOS", currentUserName);
                        Log.e("Getting ALL CONVOS2", convo.getRecipient());
                        if (convo.getRecipient().equals(currentUserName) || convo.getSender().equals(currentUserName)) {

                            convoArrayList.add(convo);
                            convoAdapter.notifyDataSetChanged();
                            setChatbotSelected();
                        }
                    }

                }),
                Throwable::printStackTrace
        );

    }


    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_searchUser);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search/Add User");
        searchView.setIconifiedByDefault(true);
        //searchView.onActionViewCollapsed();


        searchView.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {
                Log.e("On Close", "CLOSED");
                //conversationListView.setItemChecked(chatbotIndex-1,false);
                conversationListView.setAdapter(convoAdapter);
                setChatbotSelected();
                convoAdapter.notifyDataSetChanged();
                //System.out.println(conversationListView.getCheckedItemPosition());


                adapterSelect = 0;
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userArrayAdapter.notifyDataSetChanged();
                conversationListView.setAdapter(userArrayAdapter);
                adapterSelect = 1;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                userArrayAdapter.getFilter().filter(newText);
                return true;
            }


        });


        return super.onCreateOptionsMenu(menu);
    }

    public static User getcurrentUser() {
        return current_user_app;
    }

    @Override

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.e("On Open", "CLOSED");
        switch (item.getItemId()) {
            case R.id.signout:
                Amplify.Auth.signOut(
                        this::signoutScreen,
                        this::onLogOutError
                );
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onLogOutError(AuthException e) {
        this.runOnUiThread(() -> {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        });
    }

    private void signoutScreen() {
        // Listen for sign out events.
        Amplify.DataStore.clear(
                () -> Log.i("MyAmplifyApp", "DataStore is cleared."),
                failure -> Log.e("MyAmplifyApp", "Failed to clear DataStore.")
        );


        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void setChatbotSelected() {

        int count = 0;
        for (Conversation c : convoArrayList
        ) {
            count = count + 1;
            if (c.getRecipient().equals("ChatBot") || c.getSender().equals("Chatbot")) {
                Log.e("Chatbot index", "Set");
                chatbotIndex = count;
            }
        }
        //conversationListView.
        conversationListView.setItemChecked(chatbotIndex - 1, true);
    }
}