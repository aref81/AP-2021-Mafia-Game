package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PoolService {
    HashMap<Role,Integer> pools;
    CopyOnWriteArrayList<Role> roles;
    boolean isEnded;

    public PoolService(ArrayList<Role> roles) {
        pools = new HashMap<>(10);
        for (Role role : roles){
            pools.put(role,0);
        }
        this.roles = new CopyOnWriteArrayList<Role>();
        this.roles.addAll(roles);
        isEnded = false;
    }

    public Role StartPool(){
        ExecutorService pool = Executors.newCachedThreadPool();
        for (Role role : roles){
            pool.execute(new PoolClient(this,role,roles));
        }

        Timer timer = new Timer(30);
        isEnded = timer.start();

        Role removedRole = checkPool();

        String results = "0The Results are :\n";
        for (int i = 1; i <= roles.size();i++){
            results += i + "-" + roles.get(i-1).getName() + " : " +pools.get(roles.get(i-1)) +"\n";
        }
        results += "\" " + removedRole.getName() + "\"" + " is voted out!\n\n";

        for (Role role : roles){
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
        Role removedRole = roles.get(0);
        for (Role role : roles){
            if (pools.get(role) > pools.get(removedRole)){
                removedRole = role;
            }
        }
        return removedRole;
    }
}
