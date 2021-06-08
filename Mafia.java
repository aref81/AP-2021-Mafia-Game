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
            setReady(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
