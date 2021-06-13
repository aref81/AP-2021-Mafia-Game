package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PoolService {
    ConcurrentHashMap<Role,Integer> pools;
    ArrayList<Role> choices;
    ArrayList<Role> voters;
    ArrayList<Role> watchers;
    boolean isEnded;

    public PoolService(ArrayList<Role> roles,ArrayList<Role> watchers) {
        this.watchers = watchers;
        pools = new ConcurrentHashMap<>();
        for (Role role : roles){
            pools.put(role,0);
        }
        choices = new ArrayList<>();
        choices.addAll(roles);
        voters = roles;
        isEnded = false;
    }

    public PoolService(ArrayList<CitizenRole> citizens,ArrayList <MafiaRole> mafias,ArrayList<Role> watchers){
        this.watchers = watchers;
        pools = new ConcurrentHashMap<>();
        for (CitizenRole citizen : citizens){
            pools.put(citizen,0);
        }
        choices = new ArrayList<>();
        choices.addAll(citizens);

        voters = new ArrayList<>();
        voters.addAll(mafias);

        isEnded = false;
    }

    public Role StartPool() throws InterruptedException {
        ExecutorService pool = Executors.newCachedThreadPool();
        for (Role role : voters){
            pool.execute(new PoolClient(this,role,choices));
        }

        Thread.sleep(30*1000);
        isEnded = false;

        Role removedRole = checkPool();

        String results = "0The Results are :\n";
        for (int i = 1; i <= choices.size();i++){
            results += i + "-" + choices.get(i-1).getName() + " : " +pools.get(choices.get(i-1)) +"\n";
        }
        results += "\" " + removedRole.getName() + "\"" + " is voted out!\n\n";

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

    public void catchVote (Role voted) {
        pools.replace(voted,pools.get(voted) + 1);
    }

    public boolean isEnded() {
        return isEnded;
    }

    private Role checkPool (){
        Role removedRole = choices.get(0);
        for (Role role : choices){
            if (pools.get(role) > pools.get(removedRole)){
                removedRole = role;
            }
        }
        return removedRole;
    }
}
