package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * implements a Chat server,
 * includes methods to control chat and chat members
 *
 * @author Mohammad Hosein Aref
 * @version 1.0
 */
public class ChatServer {
    private int time;
    private ArrayList<ChatUser> chatUsers;
    private ArrayList<Role> watchers;
    private God god;
    private boolean end;

    /**
     * initializes a chat
     *
     * @param roles members who can chat
     * @param watchers watchers of th chat(can't send message)
     * @param time time limit of chat
     * @param god god of the game,provides methods to control members
     */
    public ChatServer(ArrayList<Role> roles,ArrayList<Role> watchers,int time,God god) {
        this.chatUsers = new ArrayList<>();
        this.watchers = watchers;
        for (Role role : roles){
            chatUsers.add(role.chatUserMaker(this));
        }
        end = false;
        this.time =time;
        this.god = god;
    }

    /**
     * starts a chat server and executes
     * threads of chat members
     *
     * @throws IOException in case of wrong input, prints stack to find the problem
     * @throws InterruptedException in case of an unintended  interrupt, prints stack to find the problem
     */
    public void startChat () throws IOException, InterruptedException {
        ExecutorService pool = Executors.newCachedThreadPool();
        for (ChatUser chatUser : chatUsers){
            pool.execute(chatUser);
        }
        for (Role role : watchers){
            role.getOutput().writeObject("0***CHAT ROOM***\n");
        }
        pool.shutdown();
        Thread.sleep(time * 1000);
        end = true;
        sendToAll(null, "exitchatMode");
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
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /**
     * sends a received message to all of chat members
     *
     * @param sender member who sent message
     * @param message the message
     */
    public synchronized void sendToAll(ChatUser sender,String message) {
        Iterator<ChatUser> it = chatUsers.iterator();
        while (it.hasNext()) {
            ChatUser chatUser = it.next();
            if (chatUser != sender) {
                try {
                    chatUser.receiveMessage(message);
                }
                catch (IOException e){
                    god.removeRole(chatUser.getName());
                }
            }
        }
        Iterator<Role> iterator = watchers.iterator();
        if (message.equals("exitchatMode")) {
            while (iterator.hasNext()) {
                Role role = iterator.next();
                try {
                    role.getOutput().writeObject("0Chat time is over!\n");
                } catch (IOException e) {
                    god.removeWatcher(role);
                }
            }
        } else if (end) {
            while (iterator.hasNext()) {
                Role role = iterator.next();
                try {
                    role.getOutput().writeObject(message);
                }
                catch (IOException e){
                    god.removeWatcher(role);
                }
            }
        } else {
            while (iterator.hasNext()) {
                Role role = iterator.next();
                try {
                    role.getOutput().writeObject("0" + message + "\n");
                }
                catch (IOException e){
                    god.removeWatcher(role);
                }
            }
        }
    }

    /**
     * indicates if the time limit has met or not
     *
     * @return status of time limit
     */
    public boolean isEnd() {
        return end;
    }

    /**
     * returns the god of game to use its methods in users
     *
     * @return the god of game
     */
    public God getGod() {
        return god;
    }
}
