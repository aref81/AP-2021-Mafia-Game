package com.company;

import java.io.*;
import java.net.Socket;

public class Mafia extends MafiaRole{

    public Mafia(Socket socket,ClientsHandler clientsHandler) throws IOException, ClassNotFoundException {
        super(socket,clientsHandler);
    }

    @Override
    public void run() {
        super.run();
        ObjectOutputStream output = super.getOutput();
        ObjectInputStream input = super.getInput();

        try {
            output.writeObject("0\n YOU ARE MAFIA!\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
