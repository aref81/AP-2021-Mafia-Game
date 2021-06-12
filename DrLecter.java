package com.company;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class DrLecter extends MafiaRole{
    private boolean isSaved;

    public DrLecter(Socket socket,ClientsHandler clientsHandler,Roles role) {
        super(socket,clientsHandler,role);
        isSaved = false;
    }

    @Override
    public void run() {
        super.run();
        ObjectOutputStream output = super.getOutput();
        ObjectInputStream input = super.getInput();

        try {
            output.writeObject("0\n YOU ARE DR.LECTER!\n");
            setReady(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Role save (ArrayList<MafiaRole> mafiaRoles){
        int index = -1;
        while (index == -1) {
            try {
                String output = "1Choose someone to save :\n";
                for (int i = 1; i <= mafiaRoles.size(); i++) {
                    output += i + "-" + mafiaRoles.get(i - 1).getName() + "\n";
                }
                super.getOutput().writeObject(output);
                String str = (String) super.getInput().readObject();
                index = Integer.parseInt(str) - 1;
                if (mafiaRoles.get(index) == this && isSaved) {
                    super.getOutput().writeObject("2You can't save yourself twice!\nPLease Choose another one\n");
                    index = -1;
                } else if (mafiaRoles.get(index) == this && !isSaved) {
                    isSaved = true;
                    super.getOutput().writeObject("0You Saved yourself\nyou can't save yourself anymore!\n");
                } else {
                    super.getOutput().writeObject("0 you saved " + mafiaRoles.get(index).getName() + "\n");
                }
            } catch (IOException | ClassNotFoundException e) {
                try {
                    super.getOutput().writeObject("2Wrong input!\ntry Again\n");
                    index = -1;
                }
                catch (IOException er){
                    er.printStackTrace();
                }
            }
        }
        return mafiaRoles.get(index);
    }
}
