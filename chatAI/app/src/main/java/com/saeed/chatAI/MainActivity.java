package com.example.chatbottest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.Conversation;
import com.amplifyframework.datastore.generated.model.Message;
import com.amplifyframework.datastore.generated.model.User;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AuthUser currentUser = Amplify.Auth.getCurrentUser();

        Intent intent;
        if(currentUser == null){
            // Go to the login screen
            intent = new Intent(getApplicationContext(), LoginActivity.class);
        }else {
            // Go to the Chat screen
            intent = new Intent(getApplicationContext(), ConvoListActivity.class);
        }

        // Start activity
        startActivity(intent);
        finish();

       /* Amplify.DataStore.query(
                User.class,
                (users) -> runOnUiThread( () -> {
                            while(users.hasNext()){
                                User next = users.next();
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
}