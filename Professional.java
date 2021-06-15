package com.company;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
/**
 * implements a Professional role in game
 * extends Citizen roles,contains actions of Professional role
 *
 * @author Mohammad Hosein Aref
 * @version 1.0
 *
 */
public class Professional extends CitizenRole{

    /**
     * initializes a Doctor
     *
     * @param socket the socket to client
     * @param clientsHandler clientHandler which contains methods to control clients
     * @param role the role of object in the game
     */
    public Professional(Socket socket,ClientsHandler clientsHandler,Roles role) {
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
            output.writeObject("0\n YOU ARE PROFESSIONAL!\n");
            setReady(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * performs the action of professional
     *
     * @param roles1 the roles in the game
     * @return the shot role,null if not shot
     */
    public Role Shoot (ArrayList<Role> roles1){
        ArrayList<Role> roles = new ArrayList<>(roles1.size() - 1);
        for (Role role : roles1){
            if (role != this){
                roles.add(role);
            }
        }

        try {
            super.getOutput().writeObject("302Do you want to shoot ?\n1.yes 2.no\n");
            int choice = Integer.parseInt((String) super.getInput().readObject());
            if (choice == 1) {
                String output = "3" + (roles.size()>9?"":"0") +roles.size() + "Choose someone to shoot :\n";
                for (int i = 1; i <= roles.size(); i++) {
                    output += i + "-" + roles.get(i - 1).getName() + "\n";
                }
                super.getOutput().writeObject(output);
                int index = Integer.parseInt((String) super.getInput().readObject()) - 1;
                Role shot = roles.get(index);
                if (shot instanceof MafiaRole) {
                    return shot;
                } else {
                    return this;
                }
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
