package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket socket = new ServerSocket(8585);
        ArrayList<Roles> rolesNames = Roles.randomize();
        ArrayList<Role> roles = new ArrayList<>(10);
        ClientsHandler clients = new ClientsHandler(roles);
        ExecutorService pool = Executors.newCachedThreadPool();

        for (Roles roleName : rolesNames ){
            try (Socket client  = socket.accept()){
                switch (roleName){
                    case MAFIA -> roles.add(new Mafia(client,clients));
                    case MAYOR -> roles.add(new Mayor(client,clients));
                    case DOCTOR -> roles.add(new Doctor(client,clients));
                    case CITIZEN -> roles.add(new Citizen(client,clients));
                    case DETECTORE -> roles.add(new Detector(client,clients));
                    case DRLECTERE -> roles.add(new DrLecter(client,clients));
                    case GODFATHER -> roles.add(new GodFather(client,clients));
                    case TOUGHLIFE -> roles.add(new ToughLife(client,clients));
                    case PROFESSIONAL -> roles.add(new Professional(client,clients));
                    case PSYCHOLOGIST -> roles.add(new Psychologist(client,clients));
                }
            }
            pool.execute(roles.get(roles.size() - 1));
        }
        pool.shutdown();
    }
}
