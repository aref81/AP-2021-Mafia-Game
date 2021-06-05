package com.company;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientsHandler {
    private ArrayList<Role> roles;
    private boolean gameState;
    private ExecutorService pool;
    private int playersNum;
    private CopyOnWriteArrayList<Roles> rolesNames;

    public ClientsHandler(int playersNum) {
        this.roles = new ArrayList<Role>(playersNum);
        this.rolesNames = Roles.randomize(playersNum);
        gameState = false;
        pool = Executors.newCachedThreadPool();
        this.playersNum = playersNum;
    }

    protected boolean checkName(String name){
        for (Role tmpRole : roles){
            if (tmpRole.getName() == null){
                continue;
            }
            if (tmpRole.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public synchronized boolean getGameState() {
        if (playersNum > roles.size()){
            gameState = false;
        }
        else {
            for (Role role : roles) {
                if (!role.isAlive()) {
                    gameState = false;
                    break;
                } else {
                    gameState = true;
                }
            }
        }
        if (gameState) {
            pool.shutdown();
        }
        return gameState;
    }

    public void add(Socket client) throws IOException, ClassNotFoundException {
        Roles roleName = rolesNames.get(0);
        rolesNames.remove(roleName);
        switch (roleName){
            case MAFIA -> roles.add(new Mafia(client,this));
            case MAYOR -> roles.add(new Mayor(client,this));
            case DOCTOR -> roles.add(new Doctor(client,this));
            case CITIZEN -> roles.add(new Citizen(client,this));
            case DETECTORE -> roles.add(new Detector(client,this));
            case DRLECTERE -> roles.add(new DrLecter(client,this));
            case GODFATHER -> roles.add(new GodFather(client,this));
            case TOUGHLIFE -> roles.add(new ToughLife(client,this));
            case PROFESSIONAL -> roles.add(new Professional(client,this));
            case PSYCHOLOGIST -> roles.add(new Psychologist(client,this));
        }
        pool.execute(roles.get(roles.size()-1));
    }

    public void remove(Role role){
        roles.remove(role);
        if (role instanceof Mafia){
            rolesNames.add(Roles.MAFIA);
        }
        else  if (role instanceof Citizen){
            rolesNames.add(Roles.CITIZEN);
        }
        else  if (role instanceof Detector){
            rolesNames.add(Roles.DETECTORE);
        }
        else  if (role instanceof Doctor){
            rolesNames.add(Roles.DOCTOR);
        }
        else  if (role instanceof DrLecter){
            rolesNames.add(Roles.DRLECTERE);
        }
        else  if (role instanceof GodFather){
            rolesNames.add(Roles.GODFATHER);
        }
        else  if (role instanceof Mayor){
            rolesNames.add(Roles.MAYOR);
        }
        else  if (role instanceof Professional){
            rolesNames.add(Roles.PROFESSIONAL);
        }
        else  if (role instanceof Psychologist){
            rolesNames.add(Roles.PSYCHOLOGIST);
        }
        else  if (role instanceof ToughLife){
            rolesNames.add(Roles.TOUGHLIFE);
        }
    }

    public CopyOnWriteArrayList<Roles> getRolesNames() {
        return rolesNames;
    }
}
