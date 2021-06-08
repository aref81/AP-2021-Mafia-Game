package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private ArrayList<ChatUser> chatUsers;
    private boolean deadline;


    public ChatServer(ArrayList<Role> roles) {
        this.chatUsers = new ArrayList<>(8);
        for (Role role : roles){
            chatUsers.add(role.chatUserMaker(this));
        }
        deadline = false;
    }

    public void startChat () {
        ExecutorService pool = Executors.newCachedThreadPool();
        for (ChatUser chatUser : chatUsers){
            pool.execute(chatUser);
        }
    }

    public void sendToAll(ChatUser sender,String message) throws IOException {
        for (ChatUser chatUser : chatUsers){
            if (chatUser != sender){
                chatUser.receiveMessage(message);
            }
        }
    }

    public boolean isDeadline() {
        return deadline;
    }
}
