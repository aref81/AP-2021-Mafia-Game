package com.company;

import java.net.Socket;
/**
 * implements a Mafia Role
 *
 * classify roles
 *
 * @author Mohammad Hosein Aref
 * @version 1.0
 */
public abstract class MafiaRole extends Role{
    /**
     * initializes a Mafia Role
     *
     * @param socket the socket to client
     * @param clientsHandler clientHandler which contains methods to control clients
     * @param role the role of object in the game
     */
    public MafiaRole(Socket socket,ClientsHandler clientsHandler,Roles role){
        super(socket,clientsHandler,role);
    }
}
