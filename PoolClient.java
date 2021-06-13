package com.company;

import java.io.IOException;
import java.util.ArrayList;

public class PoolClient implements Runnable{
    PoolService poolService;
    Role role;
    ArrayList<Role> roles;

    public PoolClient(PoolService poolService, Role role, ArrayList<Role> roles) {
        this.poolService = poolService;
        this.role = role;
        this.roles = new ArrayList<Role>();
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
            String str = (String) role.getInput().readObject();
            if(!poolService.isEnded){
                poolService.catchVote(roles.get(Integer.parseInt(str) - 1));
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
