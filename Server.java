package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket socket = new ServerSocket(8585);
        ClientsHandler clients = new ClientsHandler(10);

        for (Roles roleName : clients.getRolesNames() ){
            Socket client  = socket.accept();
            clients.add(client);
        }
    }
}
