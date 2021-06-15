package com.company;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
/**
 * implements a Mafia role in game
 * extends Mafia roles,contains actions of Maafia role
 *
 * @author Mohammad Hosein Aref
 * @version 1.0
 *
 */
public class Mafia extends MafiaRole{
    /**
     * initializes a Mafia
     *
     * @param socket the socket to client
     * @param clientsHandler clientHandler which contains methods to control clients
     * @param role the role of object in the game
     */
    public Mafia(Socket socket,ClientsHandler clientsHandler,Roles role) {
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
            output.writeObject("0\n YOU ARE MAFIA!\n");
            setReady(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
