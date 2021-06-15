package com.company;

import java.io.*;
import java.net.Socket;

/**
 * implements a Role in game
 * contains methods and info for
 * a single role
 * extends Runnable for Multi-Threading
 *
 * @author Mohammad Hosein Aref
 * @version 1.0
 *
 */
public abstract class Role implements Runnable {
    private boolean alive;
    private String name;
    private Roles role;
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ClientsHandler clientsHandler;
    private boolean ready;

    /**
     * initializes a Role
     *
     * @param socket the socket to client
     * @param clientsHandler clientHandler which contains methods to control clients
     * @param role the role of object in the game
     */
    public Role (Socket socket,ClientsHandler clientsHandler,Roles role) {
        alive = false;
        this.socket = socket;
        this.clientsHandler = clientsHandler;
        this.role = role;
        ready = false;
    }

    /**
     * returns the name of client
     *
     * @return name of client
     */
    public String getName() {
        return name;
    }

    /**
     * makes object ready to start the game
     * sets the name,..
     *
     */
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

    /**
     * returns the status of a Role in game
     *
     * @return true if alive,false if dead
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * returns the output of client
     *
     * @return output of client
     */
    public ObjectOutputStream getOutput() {
        return output;
    }

    /**
     * returns the input of client
     *
     * @return input of client
     */
    public ObjectInputStream getInput() {
        return input;
    }

    /**
     * returns the role of client
     *
     * @return the role of client
     */
    public Roles getRole() {
        return role;
    }

    /**
     * returns the status of role
     *
     * @return true if its ready for next task,false if its not ready
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * changes the status of role
     *
     * @param ready the new status
     */
    protected void setReady(boolean ready) {
        this.ready = ready;
    }

    /**
     * creates a member for chat with information of this role
     *
     * @param server the server of chat
     * @return the chat member object
     */
    public ChatUser chatUserMaker(ChatServer server){
        return new ChatUser(server,input,output,name);
    }

    /**
     * performs the action of dying for role
     *
     * indicates if client wants to be a watcher or not
     *
     * @return true if watcher,false if don't
     */
    public boolean die(){
        try {
            alive = false;
            if (connected()) {
                output.writeObject("quit");
                int choice = Integer.parseInt((String) input.readObject());
                if (choice == 1) {
                    output.writeObject("exit");
                    socket.close();
                    return false;
                } else {
                    return true;
                }
            }
        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * checks if its connected to the socket or not
     *
     * @return true if it is, false if its not
     */
    private boolean connected () {
        return !socket.isClosed();
    }
}
