package com.example.chatbottest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.auth.AuthException;
import com.amplifyframework.auth.result.AuthSignInResult;
import com.amplifyframework.auth.result.AuthSignUpResult;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.datastore.DataStoreException;
import com.amplifyframework.datastore.DataStoreItemChange;
import com.amplifyframework.datastore.generated.model.User;

public class EmailConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_confirmation);
    }

    public void onConfirmButtonPressed(View view) {
        EditText txtConfirmationCode = findViewById(R.id.txtConfirmationCode);

        Amplify.Auth.confirmSignUp(
                getEmail(),
                txtConfirmationCode.getText().toString(),
                this::onSuccess,
                this::onError
        );


    }

    private void onError(AuthException e) {
        this.runOnUiThread(()-> {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        });
    }

    private void onSaveError(DataStoreException e) {
        this.runOnUiThread(()-> {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        });
    }
    private void onSuccess(AuthSignUpResult authSignUpResult) {


        reLogin();
    }

    private void reLogin() {
        String username = getEmail();
        String password = getPassword();

        Amplify.Auth.signIn(
                username,
                password,
                this::onLoginSuccess,
                this::onError

        );
    }

    private void onLoginSuccess(AuthSignInResult authSignInResult) {
        String userId = Amplify.Auth.getCurrentUser().getUserId();

        String name = getName();
        User user1 = User.builder().name(name).id(userId).build();

        Log.e("Login Success", "User adding");
        Amplify.DataStore.save(
                user1,
                this::onSavedSuccess,
                this::onSaveError
        );
    }

    private <T extends Model> void onSavedSuccess(DataStoreItemChange<T> tDataStoreItemChange) {
        Log.e("User storage", "User properly stored");
        Intent intent = new Intent(this, ConvoListActivity.class);
        startActivity(intent);
    }

    private String getName() {
        return getIntent().getStringExtra("name");
    }

    private String getPassword() {
        return getIntent().getStringExtra("password");
    }

    private String getEmail() {
        return getIntent().getStringExtra("email");

    }
}