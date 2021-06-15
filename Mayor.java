package com.company;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
/**
 * implements a Mayor role in game
 * extends Citizen roles,contains actions of Mayor role
 *
 * @author Mohammad Hosein Aref
 * @version 1.0
 *
 */
public class Mayor extends CitizenRole{
    /**
     * initializes a Mayor
     *
     * @param socket the socket to client
     * @param clientsHandler clientHandler which contains methods to control clients
     * @param role the role of object in the game
     */
    public Mayor(Socket socket,ClientsHandler clientsHandler,Roles role) {
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
            output.writeObject("0\n YOU ARE MAYOR!\n");
            setReady(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * performs the action of veto
     *
     * @param role the voted role
     * @return null if vetoed,role if not
     */
    public Role veto (Role role){
        try {
            super.getOutput().writeObject("302Do you want to veto th pool?\n1.yes 2.no\n");
            int choice = Integer.parseInt((String) super.getInput().readObject());
            if (choice == 1) {
                return null;
            } else {
                return role;
            }
        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }
}
