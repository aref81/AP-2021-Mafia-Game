package com.company;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
/**
 * implements a GodFather role in game
 * extends Mafia roles,contains actions of GodFather role
 *
 * @author Mohammad Hosein Aref
 * @version 1.0
 *
 */
public class GodFather extends MafiaRole{
    /**
     * initializes a GodFather
     *
     * @param socket the socket to client
     * @param clientsHandler clientHandler which contains methods to control clients
     * @param role the role of object in the game
     */
    public GodFather(Socket socket,ClientsHandler clientsHandler,Roles role) {
        super(socket,clientsHandler,role);
    }
    /**
     * sets this role ready for play
     *
     */
    @Override
    public void run() {
        super.run();
        ObjectOutputStream output = super.getOutput();

        try {
            output.writeObject("0\n YOU ARE GODFATHER!\n");
            setReady(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * performs the action of god father
     *
     * @param citizens the citizens in the game
     * @return the shot citizen
     */
    public Role shoot (ArrayList<CitizenRole> citizens) {
        String str = "3" + (citizens.size()>9?"":"0") +citizens.size()+"\n";
        for (int i=1; i <= citizens.size() ; i++){
            str += i + "-" + citizens.get(i-1).getName() + "\n";
        }
        str += "Choose Which One To Shot :\n";
        int index = 0;
        try {
            super.getOutput().writeObject(str);
            index = Integer.parseInt((String) super.getInput().readObject());
        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return citizens.get(index - 1);
    }
}
