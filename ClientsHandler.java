package com.company;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientsHandler {
    private ArrayList<Role> roles;
    private boolean gameState;

    public ClientsHandler(ArrayList<Role> roles) {
        this.roles = roles;
        gameState = false;
    }

    protected boolean checkName(String name){
        for (Role role : roles){
            if (role.getName().equals(name)){
                return false;
            }
        }
        return true;
    }

    public boolean isGameState() {
        return gameState;
    }
}
