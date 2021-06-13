package com.company;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Psychologist extends CitizenRole{

    public Psychologist(Socket socket,ClientsHandler clientsHandler,Roles role) {
        super(socket,clientsHandler,role);
    }

    @Override
    public void run() {
        super.run();
        ObjectOutputStream output = super.getOutput();
        ObjectInputStream input = super.getInput();

        try {
            output.writeObject("0\n YOU ARE PSYCHOLOGIST!\n");
            setReady(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Role mute (ArrayList<Role> roles1){
        ArrayList<Role> roles = new ArrayList<>(roles1.size() - 1);
        for (Role role : roles1){
            if (role == this){
                roles.add(role);
            }
        }

        try {
            super.getOutput().writeObject("1Do you want to mute someone ?\n1.yes 2.no\n");
            int choice = Integer.parseInt((String) super.getInput().readObject());
            if (choice == 1) {
                String output = "1Choose someone to mute :\n";
                for (int i = 1; i <= roles.size(); i++) {
                    output += i + "-" + roles.get(i - 1).getName() + "\n";
                }
                super.getOutput().writeObject(output);
                int index = Integer.parseInt((String) super.getInput().readObject()) - 1;

                return roles.get(index);
            } else {
                return null;
            }
        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }
}
