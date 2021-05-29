package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(8585);
        ArrayList<Roles> rolesNames = Roles.randomize();
        ArrayList<Role> roles = new ArrayList<>(10);

        for (Roles roleName : rolesNames ){
            try (Socket client  = socket.accept()){
                switch (roleName){
                    case MAFIA -> roles.add(new Mafia(client));
                    case MAYOR -> roles.add(new Mayor(client));
                    case DOCTOR -> roles.add(new Doctor(client));
                    case CITIZEN -> roles.add(new Citizen(client));
                    case DETECTORE -> roles.add(new Detector(client));
                    case DRLECTERE -> roles.add(new DrLecter(client));
                    case GODFATHER -> roles.add(new GodFather(client));
                    case TOUGHLIFE -> roles.add(new ToughLife(client));
                    case PROFESSIONAL -> roles.add(new Professional(client));
                    case PSYCHOLOGIST -> roles.add(new Psychologist(client));
                }
            }
        }

    }

}
