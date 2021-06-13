package com.company;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Mayor extends CitizenRole{

    public Mayor(Socket socket,ClientsHandler clientsHandler,Roles role) {
        super(socket,clientsHandler,role);

    }

    @Override
    public void run() {
        super.run();
        ObjectOutputStream output = super.getOutput();
        ObjectInputStream input = super.getInput();

        try {
            output.writeObject("0\n YOU ARE MAYOR!\n");
            setReady(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Role veto (Role role){
        try {
            super.getOutput().writeObject("1Do you want to veto th pool?\n1.yes 2.no\n");
            int choice = Integer.parseInt((String) super.getInput().readObject());
            if (choice == 1) {
                return null;
            } else {
                return role;
            }
        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }
}
