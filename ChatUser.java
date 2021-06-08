package com.company;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ChatUser implements Runnable{
    private ChatServer server;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String name;

    public ChatUser(ChatServer server, ObjectInputStream input, ObjectOutputStream output, String name) {
        this.server = server;
        this.input = input;
        this.output = output;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            output.writeObject("chatMode");
            while (!server.isDeadline()){
                String message = (String) input.readObject();
                message = name + " : " + message;
                server.sendToAll(this,message);
            }
            output.writeObject("exitchatMode");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void receiveMessage(String message) throws IOException {
        output.writeObject(message);
    }
}
