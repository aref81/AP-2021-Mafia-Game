package com.company;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class GodFather extends MafiaRole{

    public GodFather(Socket socket,ClientsHandler clientsHandler,Roles role) {
        super(socket,clientsHandler,role);
    }

    @Override
    public void run() {
        super.run();
        ObjectOutputStream output = super.getOutput();
        ObjectInputStream input = super.getInput();

        try {
            output.writeObject("0\n YOU ARE GODFATHER!\n");
            setReady(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Role shoot (ArrayList<CitizenRole> citizens) {
        String str = "1\n";
        for (int i=1; i <= citizens.size() ; i++){
            str += i + "-" + citizens.get(i-1).getName() + "\n";
        }
        str += "Choose Which One To Shot :\n";
        String index = null;
        try {
            super.getOutput().writeObject(str);
            index = (String) super.getInput().readObject();
        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return citizens.get(Integer.parseInt(index) - 1);
    }
}
