package com.company;

import java.io.IOException;
import java.util.ArrayList;

/**
 * implements a pool system member
 * contains fields and methods to
 * connect and communicate with a client
 *
 * extends Runnable for Multi-Threading
 *
 * @author Mohammad Hosein Aref
 * @version 1.0
 */
public class PoolClient implements Runnable{
    private PoolService poolService;
    private Role role;
    private ArrayList<Role> roles;

    /**
     * initializes a pool client
     *
     * @param poolService the server
     * @param role the role of client in the game
     * @param roles the collection of roles in the game
     *
     */
    public PoolClient(PoolService poolService, Role role, ArrayList<Role> roles) {
        this.poolService = poolService;
        this.role = role;
        this.roles = new ArrayList<>();
        this.roles.addAll(roles);
        this.roles.remove(role);
    }

    /**
     * performs voting action
     *
     */
    @Override
    public void run() {
        String output = "Please vote to anyone you are suspicious to\nYou Got 30s\n\n";
        for (int i = 1; i <= roles.size();i++){
            output += i + "-" + roles.get(i-1).getName() + "\n";
        }
        output += "Please enter number of your vote(or don't enter anything if you don't want to vote):\n";
        try {
            role.getOutput().writeObject("pool");
            Thread.sleep(50);
            role.getOutput().writeObject(output);
            int index = Integer.parseInt((String) role.getInput().readObject()) - 1;
            if(index != -1) {
                poolService.catchVote(roles.get(index),role);
                role.getOutput().writeObject("0You Voted Successfully!\n");
            }
            role.setReady(true);
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
