package com.company;

import java.net.Socket;

/**
 * implements a Citizen Role
 *
 * classify roles
 *
 * @author Mohammad Hosein Aref
 * @version 1.0
 */
public abstract class CitizenRole extends Role{
    /**
     * initializes a Citizen Role
     *
     * @param socket the socket to client
     * @param clientsHandler clientHandler which contains methods to control clients
     * @param role the role of object in the game
     */
    public CitizenRole(Socket socket,ClientsHandler clientsHandler,Roles role){
        super(socket,clientsHandler,role);
    }
}
