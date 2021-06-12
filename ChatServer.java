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
        pool.shutdown();
        Timer timer = new Timer(60);
        deadline = timer.start();
        try {
            sendToAll(null, "exitchatMode");
        } catch (IOException e){
            e.printStackTrace();
        }
        boolean finished = false;
        int i = 0;
        try {
            while (!finished){
                Thread.sleep(1000);
                for (ChatUser chatUser : chatUsers) {
                    System.err.print("");
                    finished = chatUser.isReady();
                    if (!finished) {
                        break;
                    }
                }
                if (!finished){
                    sendToAll(null,"0\rPlease wait until other players are ready to vote" + ((i%3 == 0)?".":((i%3 == 1)?"..":"...")));
                }
                else {
                    sendToAll(null,"0\rHeading to vote!\n");
                }
                i%=999;
                i++;
            }
        }
        catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }

    public synchronized void sendToAll(ChatUser sender,String message) throws IOException {
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
