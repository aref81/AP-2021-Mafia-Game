package com.company;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
/**
 * implements a Doctor role in game
 * extends Citizen roles,contains actions of Doctor role
 *
 * @author Mohammad Hosein Aref
 * @version 1.0
 *
 */
public class Doctor extends CitizenRole{
    private boolean isSaved;
    /**
     * initializes a Doctor
     *
     * @param socket the socket to client
     * @param clientsHandler clientHandler which contains methods to control clients
     * @param role the role of object in the game
     */
    public Doctor(Socket socket,ClientsHandler clientsHandler,Roles role) {
        super(socket,clientsHandler,role);
        isSaved = false;
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
            output.writeObject("0\n YOU ARE DOCTOR!\n");
            setReady(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * performs the action of doctor
     *
     * @param roles the roles in the game
     * @return the role that doctor saved
     */
    public Role save (ArrayList<Role> roles){
        int index = -1;
        while (index == -1) {
            try {
                String output = "3" + (roles.size()>9?"":"0") +roles.size() + "Choose someone to save :\n";
                for (int i = 1; i <= roles.size(); i++) {
                    output += i + "-" + roles.get(i - 1).getName() + "\n";
                }
                super.getOutput().writeObject(output);
                String str = (String) super.getInput().readObject();
                index = Integer.parseInt(str) - 1;
                if (roles.get(index) == this && isSaved) {
                    super.getOutput().writeObject("2You can't save yourself twice!\nPLease Choose another one\n");
                    index = -1;
                } else if (roles.get(index) == this && !isSaved) {
                    isSaved = true;
                    super.getOutput().writeObject("0You Saved yourself\nyou can't save yourself anymore!\n");
                } else {
                    super.getOutput().writeObject("0 you saved " + roles.get(index).getName() + "\n");
                }
            } catch (IOException | ClassNotFoundException e) {
                try {
                    super.getOutput().writeObject("2Wrong input!\ntry Again\n");
                    index = -1;
                }
                catch (IOException er){
                    er.printStackTrace();
                }
            }
        }
        return roles.get(index);
    }
}
