package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.ServerNotActiveException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket socket = new ServerSocket(8585);
        ClientsHandler clients = new ClientsHandler(10);

        for (Roles roleName : clients.getRolesNames() ) {
            Socket client = socket.accept();
            clients.add(client);
        }
        Thread.sleep(60*1000);
        if (clients.getGameState()){
            while (clients.getGameState()) {
                Thread.sleep(1000 * 60);
            }
        }
        else {
            System.err.println("Game didn't start after 60 secs\n\nServer is Shutting Down!");
        }
    }
}
