package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * implements the main server of game
 * accepts and connects to clients
 * creates the client handler to start the game
 *
 * @author Mohammad Hosein Aref
 * @version 1.0
 *
 */
public class Server {
    /**
     * starts a server
     *
     * @param args
     * @throws IOException prints trace to find the problem
     * @throws InterruptedException prints trace to find the problem
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket socket = new ServerSocket(8585);
        ClientsHandler clients = new ClientsHandler(10);

        for (Roles roleName : clients.getRolesNames() ) {
            Socket client = socket.accept();
            clients.add(client);
        }
//        Thread.sleep(60*1000);
//        if (clients.getGameState()){
//            while (clients.getGameState()) {
//                Thread.sleep(1000 * 60);
//            }
//        }
//        else {
//            System.err.println("Game didn't start after 60 secs\n\nServer is Shutting Down!");
//        }

        while (true){
            Thread.sleep(60000);
        }
    }
}
