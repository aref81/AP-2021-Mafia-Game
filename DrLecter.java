package com.company;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
/**
 * implements a DrLecter role in game
 * extends Mafia roles,contains actions of DrLecter role
 *
 * @author Mohammad Hosein Aref
 * @version 1.0
 *
 */

public class DrLecter extends MafiaRole{
    private boolean isSaved;

    /**
     * initializes a DrLecter
     *
     * @param socket the socket to client
     * @param clientsHandler clientHandler which contains methods to control clients
     * @param role the role of object in the game
     */
    public DrLecter(Socket socket,ClientsHandler clientsHandler,Roles role) {
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
            output.writeObject("0\n YOU ARE DR.LECTER!\n");
            setReady(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * performs the action of DrLecter
     *
     * @param mafiaRoles the mafia roles in the game
     * @return the role that DrLecter saved
     */
    public Role save (ArrayList<MafiaRole> mafiaRoles){
        int index = -1;
        while (index == -1) {
            try {
                String output = "3" + (mafiaRoles.size()>9?"":"0") +mafiaRoles.size() + "Choose someone to save :\n";
                for (int i = 1; i <= mafiaRoles.size(); i++) {
                    output += i + "-" + mafiaRoles.get(i - 1).getName() + "\n";
                }
                super.getOutput().writeObject(output);
                String str = (String) super.getInput().readObject();
                index = Integer.parseInt(str) - 1;
                if (mafiaRoles.get(index) == this && isSaved) {
                    super.getOutput().writeObject("2You can't save yourself twice!\nPLease Choose another one\n");
                    index = -1;
                } else if (mafiaRoles.get(index) == this && !isSaved) {
                    isSaved = true;
                    super.getOutput().writeObject("0You Saved yourself\nyou can't save yourself anymore!\n");
                } else {
                    super.getOutput().writeObject("0 you saved " + mafiaRoles.get(index).getName() + "\n");
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
        return mafiaRoles.get(index);
    }
}
