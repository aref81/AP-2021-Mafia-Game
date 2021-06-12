package com.company;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Detector extends CitizenRole{

    public Detector(Socket socket,ClientsHandler clientsHandler,Roles role){
        super(socket,clientsHandler,role);
    }

    @Override
    public void run() {
        super.run();
        ObjectOutputStream output = super.getOutput();
        ObjectInputStream input = super.getInput();

        try {
            output.writeObject("0\n YOU ARE DETECTOR!\n");
            setReady(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void investigate (ArrayList<Role> roles){
        int choice = -1;
        while (choice == -1) {
            try {
                String output = "1";
                for (int i = 1; i < roles.size(); i++) {
                    output += i + "-" + roles.get(i - 1).getName() + "\n";
                }
                output += "\nPlease Choose a person to investigate :\n";
                super.getOutput().writeObject(output);
                choice = Integer.parseInt((String) super.getInput().readObject()) - 1;
                if (roles.get(choice) instanceof CitizenRole || roles.get(choice) instanceof GodFather){
                    super.getOutput().writeObject("0Investigation result is FALSE!\n");
                }
                else {
                    super.getOutput().writeObject("0Investigation result is TRUE!\n");
                }
            }
            catch (IOException | ClassNotFoundException e){
                try {
                    super.getOutput().writeObject("2Wrong input!\ntry Again\n");
                    choice = -1;
                }
                catch (IOException er){
                    er.printStackTrace();
                }
            }
        }
    }
}
