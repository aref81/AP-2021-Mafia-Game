package com.company;

import java.io.IOException;
import java.net.Socket;

public class Professional extends CitizenRole{

    public Professional(Socket socket,ClientsHandler clientsHandler) throws IOException, ClassNotFoundException {
        super(socket,clientsHandler);
        output.writeObject(Roles.PROFESSIONAL);
    }

    @Override
    public void run() {
        super.run();
        while (!clientsHandler.isGameState()){
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            output.writeObject("The Game Starts Now!\n");
            output.writeObject(Roles.PROFESSIONAL);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
