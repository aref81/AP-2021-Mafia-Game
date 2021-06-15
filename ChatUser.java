package com.company;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * implements a chat member
 * includes methods to control a member
 * extends thread for multi-threading
 *
 * @author Mohammad Hosein Aref
 * @version 1.0
 */
public class ChatUser extends Thread{
    private ChatServer server;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String name;
    private boolean ready;

    /**
     * initializes a chat member
     *
     * @param server the server of chat
     * @param input input stream of member
     * @param output output stream of member
     * @param name the name of member
     */
    public ChatUser(ChatServer server, ObjectInputStream input, ObjectOutputStream output, String name) {
        this.server = server;
        this.input = input;
        this.output = output;
        this.name = name;
        ready = false;
    }

    /**
     * run method,starts the action of a chat member
     *
     */
    @Override
    public void run() {
        try {
            output.writeObject("chatMode");
        } catch (IOException e){
            server.getGod().removeRole(name);
        }
        while (!server.isEnd()){
            try {
                String message = (String) input.readObject();
                if (!message.equals("")) {
                    message = name + " : " + message;
                    server.sendToAll(this, message);
                }
            }catch (IOException e) {
                server.getGod().removeRole(name);
            }
            catch (ClassNotFoundException e){
                e.printStackTrace();
            }
        }
        ready = true;
    }

    /**
     * receives a message and send it to client
     *
     * @param message the message
     * @throws IOException removes member
     */
    public void receiveMessage(String message) throws IOException {
        output.writeObject(message);
    }

    /**
     * indicates if this member is ready to close the chat
     *
     * @return the member status
     */
    public boolean isReady() {
        return ready;
    }
}
