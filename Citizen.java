package com.company;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Citizen extends CitizenRole{

    public Citizen(Socket socket,ClientsHandler clientsHandler,Roles role) {
        super(socket,clientsHandler,role);

    }

    @Override
    public void run() {
        super.run();
        ObjectOutputStream output = super.getOutput();
        ObjectInputStream input = super.getInput();

        try {
            output.writeObject("0\n YOU ARE CITIZEN!\n");
            super.getGod().firstNight(this);
            synchronized (Thread.currentThread()) {
                Thread.currentThread().wait();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
