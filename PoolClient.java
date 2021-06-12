package com.company;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class PoolClient implements Runnable{
    PoolService poolService;
    Role role;
    CopyOnWriteArrayList<Role> roles;

    public PoolClient(PoolService poolService, Role role, CopyOnWriteArrayList<Role> roles) {
        this.poolService = poolService;
        this.role = role;
        this.roles = new CopyOnWriteArrayList<Role>();
        this.roles.addAll(roles);
        this.roles.remove(role);
    }

    @Override
    public void run() {
        String output = "1Please vote to anyone you are suspicious to\nYou Got 30s\n\n";
        for (int i = 1; i <= roles.size();i++){
            output += i + "-" + roles.get(i-1).getName() + "\n";
        }
        output += "Please enter number of your vote(or don't enter anything if you don't want to vote):\n";
        try {
            role.getOutput().writeObject(output);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        try {
            String str = (String) role.getInput().readObject();
            if(!poolService.isEnded){
                poolService.catchVote(roles.get(Integer.parseInt(str) - 1));
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
