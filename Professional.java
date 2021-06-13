package com.company;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Professional extends CitizenRole{

    public Professional(Socket socket,ClientsHandler clientsHandler,Roles role) {
        super(socket,clientsHandler,role);
    }

    @Override
    public void run() {
        super.run();
        ObjectOutputStream output = super.getOutput();
        ObjectInputStream input = super.getInput();

        try {
            output.writeObject("0\n YOU ARE PROFESSIONAL!\n");
            setReady(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Role Shoot (ArrayList<Role> roles1){
        ArrayList<Role> roles = new ArrayList<>(roles1.size() - 1);
        for (Role role : roles1){
            if (role == this){
                roles.add(role);
            }
        }

        try {
            super.getOutput().writeObject("1Do you want to shoot ?\n1.yes 2.no\n");
            int choice = Integer.parseInt((String) super.getInput().readObject());
            if (choice == 1) {
                String output = "1Choose someone to shoot :\n";
                for (int i = 1; i <= roles.size(); i++) {
                    output += i + "-" + roles.get(i - 1).getName() + "\n";
                }
                super.getOutput().writeObject(output);
                int index = Integer.parseInt((String) super.getInput().readObject()) - 1;
                Role shot = roles.get(index);
                if (shot instanceof MafiaRole) {
                    return shot;
                } else {
                    return this;
                }
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
