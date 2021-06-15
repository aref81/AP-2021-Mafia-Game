package com.company;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
/**
 * implements a Psychologist role in game
 * extends Citizen roles,contains actions of Psychologist role
 *
 * @author Mohammad Hosein Aref
 * @version 1.0
 *
 */
public class Psychologist extends CitizenRole{
    /**
     * initializes a Psychologist
     *
     * @param socket the socket to client
     * @param clientsHandler clientHandler which contains methods to control clients
     * @param role the role of object in the game
     */
    public Psychologist(Socket socket,ClientsHandler clientsHandler,Roles role) {
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
            output.writeObject("0\n YOU ARE PSYCHOLOGIST!\n");
            setReady(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * performs the action of Psychologist
     *
     * @param roles1 all roles in the game
     * @return the muted role,null if not
     */
    public Role mute (ArrayList<Role> roles1){
        ArrayList<Role> roles = new ArrayList<>(roles1.size() - 1);
        for (Role role : roles1){
            if (role != this){
                roles.add(role);
            }
        }

        try {
            super.getOutput().writeObject("302Do you want to mute someone ?\n1.yes 2.no\n");
            int choice = Integer.parseInt((String) super.getInput().readObject());
            if (choice == 1) {
                String output = "3" + (roles.size()>9?"":"0") +roles.size() + "Choose someone to mute :\n";
                for (int i = 1; i <= roles.size(); i++) {
                    output += i + "-" + roles.get(i - 1).getName() + "\n";
                }
                super.getOutput().writeObject(output);
                int index = Integer.parseInt((String) super.getInput().readObject()) - 1;

                return roles.get(index);
            } else {
                return null;
            }
        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }
}
