package com.company;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * make clients ready for game
 * and contain methods to control them
 *
 * @author Mohammad Hosein Aref
 * @version 1.0
 *
 */
public class ClientsHandler {
    private ArrayList<Role> roles;
    private boolean gameState;
    private ExecutorService pool;
    private int playersNum;
    private CopyOnWriteArrayList<Roles> rolesNames;

    /**
     * initializes a client Handler
     *
     * @param playersNum number of players
     */
    public ClientsHandler(int playersNum) {
        this.roles = new ArrayList<Role>(playersNum);
        this.rolesNames = Roles.randomize(playersNum);
        gameState = false;
        pool = Executors.newCachedThreadPool();
        this.playersNum = playersNum;
    }

    /**
     * checks if the name exists or not
     *
     * @param name the name
     * @return true if exist,false if don't
     */
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

    /**
     * checks and returns the status of game
     *
     * @return true if its running,false if it isn't
     */
    public synchronized boolean getGameState() {
        if (!gameState) {
            if (!(playersNum > roles.size())) {
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
                startGame();
                pool.shutdown();
            }
        }
        return gameState;
    }

    /**
     * adds a new socket to the game
     * with a role
     *
     * @param client the new client
     */
    public void add(Socket client) {
        Roles roleName = rolesNames.get(0);
        rolesNames.remove(roleName);
        switch (roleName){
            case MAFIA -> roles.add(new Mafia(client,this,roleName));
            case MAYOR -> roles.add(new Mayor(client,this,roleName));
            case DOCTOR -> roles.add(new Doctor(client,this,roleName));
            case CITIZEN -> roles.add(new Citizen(client,this,roleName));
            case DETECTORE -> roles.add(new Detector(client,this,roleName));
            case DRLECTERE -> roles.add(new DrLecter(client,this,roleName));
            case GODFATHER -> roles.add(new GodFather(client,this,roleName));
            case TOUGHLIFE -> roles.add(new ToughLife(client,this,roleName));
            case PROFESSIONAL -> roles.add(new Professional(client,this,roleName));
            case PSYCHOLOGIST -> roles.add(new Psychologist(client,this,roleName));
        }
        pool.execute(roles.get(roles.size()-1));
    }

//    public void remove(Role role){
//        roles.remove(role);
//        if (role instanceof Mafia){
//            rolesNames.add(Roles.MAFIA);
//        }
//        else  if (role instanceof Citizen){
//            rolesNames.add(Roles.CITIZEN);
//        }
//        else  if (role instanceof Detector){
//            rolesNames.add(Roles.DETECTORE);
//        }
//        else  if (role instanceof Doctor){
//            rolesNames.add(Roles.DOCTOR);
//        }
//        else  if (role instanceof DrLecter){
//            rolesNames.add(Roles.DRLECTERE);
//        }
//        else  if (role instanceof GodFather){
//            rolesNames.add(Roles.GODFATHER);
//        }
//        else  if (role instanceof Mayor){
//            rolesNames.add(Roles.MAYOR);
//        }
//        else  if (role instanceof Professional){
//            rolesNames.add(Roles.PROFESSIONAL);
//        }
//        else  if (role instanceof Psychologist){
//            rolesNames.add(Roles.PSYCHOLOGIST);
//        }
//        else  if (role instanceof ToughLife){
//            rolesNames.add(Roles.TOUGHLIFE);
//        }
//    }

    /**
     * returns the collection of roles for the game
     *
     * @return the collection of roles
     */
    public CopyOnWriteArrayList<Roles> getRolesNames() {
        return rolesNames;
    }

    /**
     * starts a game
     *
     */
    private void startGame (){
        God god = new God(roles);
        pool.execute(god);
    }
}
