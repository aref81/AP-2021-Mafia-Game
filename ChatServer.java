package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private ArrayList<ChatUser> chatUsers;
    private ArrayList<Role> watchers;
    private boolean deadline;


    public ChatServer(ArrayList<Role> roles,ArrayList<Role> watchers) {
        this.chatUsers = new ArrayList<>();
        this.watchers = watchers;
        for (Role role : roles){
            chatUsers.add(role.chatUserMaker(this));
        }
        deadline = false;
    }

    public void startChat () throws IOException, InterruptedException {
        ExecutorService pool = Executors.newCachedThreadPool();
        for (ChatUser chatUser : chatUsers){
            pool.execute(chatUser);
        }
        for (Role role : watchers){
            role.getOutput().writeObject("0***CHAT ROOM***\n");
        }
        pool.shutdown();
        Thread.sleep(60 * 1000);
        deadline = false;
        try {
            sendToAll(null, "exitchatMode");
            for (ChatUser chatUser : chatUsers){
                chatUser.interrupt();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
//        boolean finished = false;
//        int i = 0;
//        try {
//            while (!finished){
//                Thread.sleep(1000);
//                for (ChatUser chatUser : chatUsers) {
//                    System.err.print("");
//                    finished = chatUser.isReady();
//                    if (!finished) {
//                        break;
//                    }
//                }
//                if (!finished){
//                    sendToAll(null,"0\rPlease wait until other players are ready to vote" + ((i%3 == 0)?".":((i%3 == 1)?"..":"...")));
//                }
//                else {
//                    sendToAll(null,"0\rHeading to vote!\n");
//                }
//                i%=999;
//                i++;
//            }
//        }
//        catch (IOException | InterruptedException e){
//            e.printStackTrace();
//        }
    }

    public synchronized void sendToAll(ChatUser sender,String message) throws IOException {
        for (ChatUser chatUser : chatUsers){
            if (chatUser != sender){
                chatUser.receiveMessage(message);
            }
        }
        if (message.equals("exitchatMode")){
            for (Role role : watchers){
                role.getOutput().writeObject("0Chat time is over!\n");
            }
        }
        else {
            for (Role role : watchers) {
                role.getOutput().writeObject("0" + message + "\n");
            }
        }
    }

    public boolean isDeadline() {
        return deadline;
    }
}
