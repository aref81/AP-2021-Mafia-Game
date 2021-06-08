package com.company;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ToughLife extends CitizenRole{

    public ToughLife(Socket socket,ClientsHandler clientsHandler,Roles role) {
        super(socket,clientsHandler,role);
    }

    @Override
    public void run() {
        super.run();
        ObjectOutputStream output = super.getOutput();
        ObjectInputStream input = super.getInput();

        try {
            output.writeObject("0\n YOU ARE TOUGH LIFE!\n");
            setReady(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
