package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * implements a pool system
 * contains methods and fields
 * to control the voting action
 *
 * @author Mohammad Hosein Aref
 * @version 1.0
 *
 */
public class PoolService {
    private ConcurrentHashMap<Role,ArrayList<String>> pools;
    private ArrayList<Role> choices;
    private ArrayList<Role> voters;
    private ArrayList<Role> watchers;

    /**
     * initializes a pool system(for days)
     *
     * @param roles the roles in the game
     * @param watchers the watchers
     */
    public PoolService(ArrayList<Role> roles,ArrayList<Role> watchers) {
        this.watchers = watchers;
        pools = new ConcurrentHashMap<>();
        for (Role role : roles){
            pools.put(role,new ArrayList<>());
        }
        choices = new ArrayList<>();
        choices.addAll(roles);
        voters = new ArrayList<>(roles.size());
        voters.addAll(roles);
    }

    /**
     * initializes a pool system(for nights)
     *
     * @param citizens the citizens in the game (pools)
     * @param mafias the mafias in the game (voters)
     * @param watchers the watchers
     */
    public PoolService(ArrayList<CitizenRole> citizens,ArrayList <MafiaRole> mafias,ArrayList<Role> watchers){
        this.watchers = watchers;
        pools = new ConcurrentHashMap<>();
        for (CitizenRole citizen : citizens){
            pools.put(citizen,new ArrayList<>());
        }
        choices = new ArrayList<>();
        choices.addAll(citizens);

        voters = new ArrayList<>();
        voters.addAll(mafias);
    }

    /**
     * sarts a voting a ction
     *
     * @return the voted role
     * @throws InterruptedException prints trace foe finding problem
     */
    public Role StartPool() throws InterruptedException {
        ExecutorService pool = Executors.newCachedThreadPool();
        for (Role role : voters){
            pool.execute(new PoolClient(this,role,choices));
        }

        boolean finished = false;
        while (!finished){
                Thread.sleep(1000);
                for (Role role : voters) {
                    System.err.print("");
                    finished = role.isReady();
                    if (!finished) {
                        break;
                    }
                }
        }
        for (Role role : voters){
            role.setReady(false);
        }

        Role removedRole = checkPool();

        String results = "0The Results are :\n";
        for (int i = 1; i <= choices.size();i++){
            results += i + "-" + choices.get(i-1).getName() + " : " +pools.get(choices.get(i-1)) +"\n";
        }
        if (removedRole != null) {
            results += "\" " + removedRole.getName() + "\"" + " is voted out!\n\n";
        }
        else {
            results += "we have equal votes\nno one is going out\n\n";
        }

        for (Role role : voters){
            try {
                role.getOutput().writeObject(results);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        for (Role role : watchers){
            try {
                role.getOutput().writeObject(results);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        return removedRole;
    }

    /**
     * catches a vote from a client
     *
     * @param voted the voted role
     * @param voter the voter role
     */
    public synchronized void catchVote (Role voted,Role voter) {
        pools.get(voted).add(voter.getName());
    }

    /**
     * checks the result of vote
     *
     * @return the vote result (null if exits equal)
     */
    private Role checkPool (){
            Role removedRole = choices.get(0);
            for (Role role : choices) {
                if (pools.get(role).size() > pools.get(removedRole).size()) {
                    removedRole = role;
                }
            }
            if (checkEqual(removedRole)) {
                return removedRole;
            }
            else return null;
    }

    /**
     * checks if there is equal votes
     *
     * @return false if yes,true if no
     */
    private boolean checkEqual (Role role){
            for (Role tempRole : choices){
                if (role == tempRole){
                    continue;
                }
                else {
                    if (pools.get(role).size() == pools.get(tempRole).size()){
                        return false;
                    }
                }
            }
        return true;
    }
}
