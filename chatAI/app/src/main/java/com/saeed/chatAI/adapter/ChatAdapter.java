package com.example.chatbottest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Message;
import com.amplifyframework.datastore.generated.model.User;
import com.example.chatbottest.R;
import com.example.chatbottest.allUsers;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends ArrayAdapter<Message> {

   // public allUsers users = new allUsers();
    public String otheruser;
    public ChatAdapter(@NonNull Context context, int resource, @NonNull List<Message> messages)
    {
        super(context,resource,messages);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Message message = getItem(position);


        boolean isCurrentUserMessage = message.getMessageSender().equals(Amplify.Auth.getCurrentUser().getUserId());
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view;
        if(isCurrentUserMessage)
        {
            view = layoutInflater.inflate(R.layout.message_sent_layout,parent,false);
        }
        else
        {
            view = layoutInflater.inflate(R.layout.message_received_layout,parent,false);
            TextView nameText = view.findViewById(R.id.name);
            nameText.setText(otheruser);
        }

        TextView messageContextText = view.findViewById(R.id.messageContent);
        TextView messageDateText = view.findViewById(R.id.messageDate);

        messageContextText.setText(
                message.getContent()
        );

        messageDateText.setText(

                message.getDate().format()
        );
        return view;
    }

}
