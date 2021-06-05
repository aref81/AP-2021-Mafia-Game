package com.company;

import java.io.*;
import java.net.Socket;

public abstract class Role implements Runnable {
    private boolean alive;
    private String name;
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ClientsHandler clientsHandler;

    public Role (Socket socket,ClientsHandler clientsHandler) {
        alive = false;
        this.socket = socket;
        this.clientsHandler = clientsHandler;
    }

    public String getName() {
        return name;
    }

    @Override
    public void run(){
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            boolean loop;
            do{
                output.writeObject("1Please choose a user name:\n");
                String tmpName = (String) input.readObject();
                loop = clientsHandler.checkName(tmpName);

                if (!(loop)){
                    name = tmpName;
                    output.writeObject("0" + name + " successfully set as your name!\n");
                }
                else {
                    output.writeObject("0Sorry\nName already exists!\nplease try again\n");
                }
            }while (loop);

            loop = true;
            while (loop) {
                output.writeObject("1Type \"ready\" whenever you are ready to play or \"exit\" to exit the game :\n");
                String tmpStr = (String) input.readObject();
                if (tmpStr.equals("ready")){
                    alive = true;
                    loop = false;
                }
                else if(tmpStr.equals("exit")){
                    clientsHandler.remove(this);
                    output.writeObject("exit");
                    socket.close();
                    return;
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
                    output.writeObject("0\nGame Starts now!\n");
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

}
