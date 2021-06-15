package com.company;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * implements a citizen role in game
 * extends citizen roles,contains actions of citizen role
 *
 * @author Mohammad Hosein Aref
 * @version 1.0
 *
 */
public class Citizen extends CitizenRole{

    /**
     * initializes a Citizen
     *
     * @param socket the socket to client
     * @param clientsHandler clientHandler which contains methods to control clients
     * @param role the role of object in the game
     */
    public Citizen(Socket socket,ClientsHandler clientsHandler,Roles role) {
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
            output.writeObject("0\n YOU ARE CITIZEN!\n");
            setReady(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
