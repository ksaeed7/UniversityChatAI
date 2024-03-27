package com.example.chatbottest;


import com.amplifyframework.datastore.generated.model.User;

import java.util.ArrayList;

public class allUsers {
    public ArrayList<User> alluserArrayList = new ArrayList<>();

    public void addUser(User user)
    {
        alluserArrayList.add(user);
    }
}
