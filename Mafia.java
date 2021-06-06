package com.company;

import java.io.*;
import java.net.Socket;

public class Mafia extends MafiaRole{

    public Mafia(Socket socket,ClientsHandler clientsHandler,Roles role) {
        super(socket,clientsHandler,role);
    }

    @Override
    public void run() {
        super.run();
        ObjectOutputStream output = super.getOutput();
        ObjectInputStream input = super.getInput();

        try {
            output.writeObject("0\n YOU ARE MAFIA!\n");

            super.getGod().firstNight(this);
            synchronized (Thread.currentThread()) {
                Thread.currentThread().wait();
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
