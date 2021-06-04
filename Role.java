package com.company;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class Role implements Runnable {
    protected boolean alive;
    protected String name;
    protected Socket socket;
    protected ObjectOutputStream output;
    protected ObjectInputStream input;
    protected ClientsHandler clientsHandler;

    public Role (Socket socket,ClientsHandler clientsHandler) throws IOException {
        alive = true;
        this.socket = socket;
        this.clientsHandler = clientsHandler;
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
    }

    public String getName() {
        return name;
    }

    @Override
    public void run(){
        try {
            boolean loop = false;
            do {
                String tmpStr = (String) input.readObject();
                loop = clientsHandler.checkName(tmpStr);
                if (loop) {
                    name = tmpStr;
                }
                output.writeObject(loop);
            } while (!loop);

            output.writeObject("Please Wait Until The Game Starts...");
        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
