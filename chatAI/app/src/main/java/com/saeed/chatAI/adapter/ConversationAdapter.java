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
import com.amplifyframework.datastore.generated.model.Conversation;

import com.amplifyframework.datastore.generated.model.User;
import com.example.chatbottest.ConvoListActivity;
import com.example.chatbottest.R;

import java.util.List;

public class ConversationAdapter extends ArrayAdapter<Conversation> {

    public ConversationAdapter(@NonNull Context context, int resource, @NonNull List<Conversation> conversations)
    {

        super(context,resource,conversations);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Conversation conversation = getItem(position);
        User current_User = ConvoListActivity.getcurrentUser();
        String currentUserName = "";
        if(current_User!=null)
            currentUserName = current_User.getName();
       // boolean isCurrentUserMessage = message.getMessageSender().equals(Amplify.Auth.getCurrentUser().getUserId());
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view;
        view = layoutInflater.inflate(R.layout.conversation_list_layout,parent,false);
        TextView convoListContent = view.findViewById(R.id.conversationListContent);

        if(conversation.getSender().equals(conversation.getRecipient())) {
            convoListContent.setText(conversation.getRecipient());
        }
        else if(conversation.getSender().equals(currentUserName) )
        {
            convoListContent.setText(conversation.getRecipient());
        }
        else if(conversation.getRecipient().equals(currentUserName))
        {
            convoListContent.setText(conversation.getSender());
        }


        return view;
    }
}
