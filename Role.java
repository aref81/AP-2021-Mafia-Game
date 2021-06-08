package com.company;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Role implements Runnable {
    private boolean alive;
    private String name;
    private Roles role;
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ClientsHandler clientsHandler;
    private boolean ready;

    public Role (Socket socket,ClientsHandler clientsHandler,Roles role) {
        alive = false;
        this.socket = socket;
        this.clientsHandler = clientsHandler;
        this.role = role;
        ready = false;
    }

    public String getName() {
        return name;
    }

    @Override
    public void run(){
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            boolean loop = true;
            do {
                try {
                    output.writeObject("1Please choose a user name:\n");
                    String tmpName = (String) input.readObject();
                    loop = clientsHandler.checkName(tmpName);

                    if (!(loop)) {
                        name = tmpName;
                        output.writeObject("0" + name + " successfully set as your name!\n");
                    } else {
                        throw new NameExistException(tmpName);
                    }
            }
            catch(NameExistException e){
                output.writeObject(e.toString());
            }
        } while (loop) ;

            loop = true;
                while (loop) {
                    try {
//                output.writeObject("1Type \"ready\" whenever you are ready to play or \"exit\" to exit the game :\n");
                        output.writeObject("1Type \"ready\" whenever you are ready to play :\n");
                        String tmpStr = (String) input.readObject();
                        if (tmpStr.equals("ready")) {
                            alive = true;
                            loop = false;
                        }
//                else if(tmpStr.equals("exit")){
//                    clientsHandler.remove(this);
//                    output.writeObject("exit");
//                    socket.close();
//                    return;
//                }
                        else {
                            throw new IOException();
                        }
                    } catch (IOException e) {
                        output.writeObject("2Invalid input!\nPlease  try again\n");
                    }
                }

            output.writeObject("0Please wait until game starts");

            loop = true;
            for (int i = 0;loop;i++) {
                Thread.sleep(1000);
                loop = !(clientsHandler.getGameState());

                if (loop){
                    output.writeObject("0\rPlease wait until game starts" + ((i%3 == 0)?".":((i%3 == 1)?"..":"...")));
                }
                else {
                    output.writeObject("0\rGame Starts now!\n");
                }

                i %=999;
            }

        }
        catch (IOException | ClassNotFoundException | InterruptedException e){
            e.printStackTrace();
        }
    }


    public boolean isAlive() {
        return alive;
    }

    public ObjectOutputStream getOutput() {
        return output;
    }

    public ObjectInputStream getInput() {
        return input;
    }

    public Roles getRole() {
        return role;
    }

    public boolean isReady() {
        return ready;
    }

    protected void setReady(boolean ready) {
        this.ready = ready;
    }

    public ChatUser chatUserMaker(ChatServer server){
        return new ChatUser(server,input,output,name);
    }
}
