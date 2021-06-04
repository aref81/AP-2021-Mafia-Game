package com.company;

import java.io.IOException;
import java.net.Socket;

public class Doctor extends CitizenRole{

    public Doctor(Socket socket,ClientsHandler clientsHandler) throws IOException, ClassNotFoundException {
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
            output.writeObject(Roles.DOCTOR);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
