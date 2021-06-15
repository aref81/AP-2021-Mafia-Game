package com.company;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ToughLife extends CitizenRole{
    private boolean hasGurd;
    private int numberOfReqs;

    public ToughLife(Socket socket,ClientsHandler clientsHandler,Roles role) {
        super(socket,clientsHandler,role);
        hasGurd = true;
        numberOfReqs = 0;
    }

    @Override
    public void run() {
        super.run();
        ObjectOutputStream output = super.getOutput();

        try {
            output.writeObject("0\n YOU ARE TOUGH LIFE!\n");
            setReady(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean deadRolesRequest (){
        int choice = 0;
        try {
            super.getOutput().writeObject("302Do you want to request?\n1.yes 2.no\n");
            choice = Integer.parseInt((String) super.getInput().readObject());
        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        if (choice == 1){
            numberOfReqs++;
            return true;
        }
        else {
            return false;
        }
    }

    public boolean hasGurd() {
        return hasGurd;
    }

    public void shot (){
        hasGurd = false;
    }

    public boolean canReq() {
        return numberOfReqs < 2;
    }
}
