package com.company;

import java.io.IOException;
import java.net.Socket;

public class Detector extends CitizenRole{

    public Detector(Socket socket,ClientsHandler clientsHandler) throws IOException, ClassNotFoundException {
        super(socket,clientsHandler);

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
            output.writeObject(Roles.DETECTORE);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
