package com.company;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * implements the god of game
 * god controls everything in game
 * contains info and methods to
 * control the whle game and roles
 *
 * @author Mohammad Hosein Aref
 * @version 1.0
 *
 *
 */
public class God implements Runnable{
    private ArrayList<Role> roles;
    private ArrayList<MafiaRole> mafias;
    private ArrayList<CitizenRole> citizens;
    private GodFather godFather;
    private Detector detector;
    private Doctor doctor;
    private DrLecter drLecter;
    private Mayor mayor;
    private Professional professional;
    private Psychologist psychologist;
    private ToughLife toughLife;
    private ArrayList<Mafia> normalMafias;
    private ArrayList<Citizen> normalCitizens;
    private ArrayList<Role> deadRoles;
    private ArrayList<Role> watchers;
    private Role shot;
    private Role voted;
    private Role proShot;
    private Role muted;
    private boolean req;
    private boolean isShot;
    private boolean day;

    /**
     * initializes the game
     * sets each role to appropriate fields
     *
     * @param roles the roles passed by client handler to start the game
     */
    public God(ArrayList<Role> roles) {
        this.roles = roles;
        mafias = new ArrayList<>(2);
        normalMafias = new ArrayList<>();

        for (Role role : roles){
            if (role instanceof MafiaRole){
                mafias.add((MafiaRole) role);
            }
            Roles roleName = role.getRole();
            switch (roleName){
                case MAFIA -> normalMafias.add((Mafia) role);
                case DRLECTERE -> drLecter = (DrLecter) role;
                case GODFATHER -> godFather = (GodFather) role;
            }
        }

        citizens = new ArrayList<>(6);
        normalCitizens = new ArrayList<>();

        for (Role role : roles){
            if (role instanceof CitizenRole){
                citizens.add((CitizenRole) role);
            }
            Roles roleName = role.getRole();
            switch (roleName){
                case MAYOR -> mayor = (Mayor) role;
                case DOCTOR -> doctor = (Doctor) role;
                case CITIZEN -> citizens.add((CitizenRole) role);
                case DETECTORE -> detector = (Detector) role;
                case TOUGHLIFE -> toughLife = (ToughLife) role;
                case PROFESSIONAL -> professional = (Professional) role;
                case PSYCHOLOGIST -> psychologist = (Psychologist) role;
            }
        }
        deadRoles = new ArrayList<>();
        watchers = new ArrayList<>();
        shot = null;
        voted = null;
        proShot = null;
        muted = null;
        req = false;
        isShot = false;
        day = false;
    }

    /**
     * starts a game
     */
    @Override
    public void run (){
        try {
            firstNight();
            gameLoop();
        }
        catch (InterruptedException | IOException e){
            e.printStackTrace();
        }
    }

    /**
     * performs the first night
     *
     * @throws InterruptedException prints trace to find the problem
     * @throws IOException prints trace to find the problem
     */
    private void firstNight () throws InterruptedException, IOException {
        boolean start = false;
        while (!start){
            for (Role role : roles) {
                System.err.print("");
                start = role.isReady();
                if (!start) {
                    break;
                }
            }
        }
        for (Role role : roles){
            role.getOutput().writeObject("0\nFirst Night Begins!\n");
        }
        for (Role role : mafias) {
            role.getOutput().writeObject("0\nMAFIA Group Wakes Up\n\nMAFIA members :\n");
            for (int i = 1; i <= mafias.size(); i++) {
                Thread.sleep(10);
                role.getOutput().writeObject("0" + i + "- " + mafias.get(i - 1).getRole() + " ---> " + mafias.get(i - 1).getName() + (mafias.get(i - 1) == role ? " (You)\n" : "\n"));
            }
            Thread.sleep(10);
            role.getOutput().writeObject("0\n\nMafia Group Sleeps.\n\n");
        }
        for (Role role : citizens) {
            if (role.getRole() == Roles.MAYOR) {
                Thread.sleep(50);
                role.getOutput().writeObject("0\nMayor Wakes Up\n" + doctor.getRole() + " ---> " + doctor.getName() + "\n");
            } else if (role.getRole() == Roles.DOCTOR) {
                Thread.sleep(50);
                role.getOutput().writeObject("0\nDoctor Wakes Up\n" + mayor.getRole() + " ---> " + mayor.getName() + "\n");
            }
        }
        for (Role role : roles) {
            role.getOutput().writeObject("0\nNight Finishes.\n\n");
            role.setReady(false);
        }
        day = true;
    }

    /**
     * executes the gameloop
     *
     * @throws IOException prints trace to find the problem
     * @throws InterruptedException prints trace to find the problem
     */
    private void gameLoop () throws IOException, InterruptedException {
        boolean loop = true;
        SecureRandom random = new SecureRandom();
        while (loop) {
            if (day) {
                sendToAll("0\nSun Rises.\nand Day begins!\n\n");

                if (isShot) {
                    if (shot != null) {
                        sendToAll("0Last night Mafia Shot " + shot.getName() + "\n");
                        removeRole(shot);
                    } else {
                        sendToAll("0Last night Mafia's bullet was missed!\n");
                    }
                    isShot = false;
                }
                if (voted != null) {
                    String message;
                    if (voted instanceof CitizenRole) {
                        message = "0Yesterday, we lost a citizen\n";
                    } else {
                        message = "0Yesterday, we executed a Mafia\n";
                    }
                    sendToAll(message);
                    voted = null;
                }
                if (proShot != null) {
                    sendToAll("0Last night Professional shot : " + proShot.getName() + "\n");
                    removeRole(proShot);
                }
                if (req){
                    if (!deadRoles.isEmpty()) {
                        StringBuilder output = new StringBuilder("0Request for dead roles received\ndead roles :\n");
                        randomizer(random, deadRoles);
                        for (int i = 1; i <= deadRoles.size(); i++) {
                            output.append(i).append("-").append(deadRoles.get(i - 1).getName()).append("\n");
                        }
                        output.append("\n");

                        sendToAll(output.toString());
                    }
                    else {
                        sendToAll("0Request for dead roles received\nNo dead roles yet!\n");
                    }
                    req = false;
                }

                if (muted != null){
                    sendToAll("0Psychologist has muted " + muted.getName() + "\n");
                    if ((muted != shot) && (muted != proShot)) {
                        watchers.add(muted);
                        roles.remove(muted);
                    }
                    else {
                        muted = null;
                        sendToAll("0But this role is dead!\n");
                    }
                }
                shot = null;
                proShot = null;

                loop = checkEndGame();
                if (!loop){
                    break;
                }

                ChatServer chatServer = new ChatServer(roles,watchers,6,this);
                chatServer.startChat();
                PoolService poolService = new PoolService(roles,watchers);
                Role mayrem = poolService.StartPool();
                if (mayrem != null) {
                    if (mayor != null) {
                        sendToAll("0Mayor now makes the decision on veto.\n");
                        mayrem = mayor.veto(mayrem);
                    }
                    if (mayrem != null) {
                        sendToAll("0Mayor didn't veto.\n");
                        removeRole(mayrem);
                    } else {
                        sendToAll("0Mayor vetoed the pool!\n");
                    }
                }
                if (muted != null) {
                    watchers.remove(muted);
                    roles.add(muted);
                    muted = null;
                }
                day = false;
            } else {
                sendToAll("0\nSun Sets.\nand Night Begins!\n\n");
                sendToAll("0Mafia Group Wakes Up.\n");
                ArrayList<Role> mafiaChat = new ArrayList<>(mafias.size());
                mafiaChat.addAll(mafias);
                ChatServer chatServer = new ChatServer(mafiaChat,watchers,3,this);
                chatServer.startChat();
                PoolService poolService = new PoolService(citizens,mafias,watchers);
                shot = poolService.StartPool();

                if (godFather != null) {
                    for (Role role : mafias) {
                        role.getOutput().writeObject("0Pool Finished!\nGodFather now decides and takes the shot!\n");
                    }
                    shot = godFather.shoot(citizens);
                    isShot = true;
                    for (Role role : mafias) {
                        role.getOutput().writeObject("0GodFather shot " + shot.getName() + "\n");
                    }
                }
                else {
                    for (Role role : mafias) {
                        role.getOutput().writeObject("0Pool Finished!\nnow Mafia take the shot!\n");
                    }
                }

                sendToAll("0Mafia Group Sleeps\n");

                Role savedMafia = null;
                sendToAll("0DrLecter Wakes Up!\n");

                if (drLecter != null) {
                    savedMafia = drLecter.save(mafias);
                }
                else {
                    Thread.sleep(random.nextInt(5000));
                }
                sendToAll("0DrLecter Sleeps.\n");

                Role savedRole = null;
                sendToAll("0Doctor Wakes Up!\n");
                if (drLecter != null) {
                    savedRole = doctor.save(roles);
                }
                else {
                    Thread.sleep(random.nextInt(5000));
                }
                sendToAll("0Doctor Sleeps.\n");

                sendToAll("0Detector Wakes Up!\n");
                if (detector != null) {
                    detector.investigate(roles);
                }
                else {
                    Thread.sleep(random.nextInt(5000));
                }
                sendToAll("0Detector Sleeps.\n");

                sendToAll("0Professional Wakes Up!\n");
                if (professional != null) {
                    proShot = professional.Shoot(roles);
                }
                else {
                    Thread.sleep(random.nextInt(5000));
                }
                sendToAll("0Professional Sleeps.\n");

                sendToAll("0Psychologist Wakes Up!\n");
                if (psychologist != null) {
                    muted = psychologist.mute(roles);
                }
                else {
                    Thread.sleep(random.nextInt(5000));
                }
                sendToAll("0Psychologist Sleeps.\n");

                sendToAll("0Tough Life Wakes Up!\n");
                if ((toughLife != null) && toughLife.canReq()) {
                    req = toughLife.deadRolesRequest();
                } else {
                    Thread.sleep(random.nextInt(5000));
                }
                sendToAll("0Tough Life Sleeps.\n");

                if (savedMafia == proShot || savedRole == proShot) {
                    proShot = null;
                }

                if (savedRole == shot) {
                    shot = null;
                }

                if (shot == toughLife) {
                    if (toughLife.hasGurd()) {
                        toughLife.shot();
                        shot = null;
                    }
                }

                day = true;
            }

            loop = checkEndGame();
        }
    }

    /**
     * removes a role from the game
     *
     * @param removedRole the removing role
     */
    public synchronized void removeRole(Role removedRole){
        deadRoles.add(removedRole);
        if (removedRole instanceof CitizenRole) {
            citizens.remove(removedRole);
            Roles roleName = removedRole.getRole();
            switch (roleName) {
                case MAYOR -> mayor = null;
                case DOCTOR -> doctor = null;
                case CITIZEN -> citizens.remove(removedRole);
                case DETECTORE -> detector = null;
                case TOUGHLIFE -> toughLife = null;
                case PROFESSIONAL -> professional = null;
                case PSYCHOLOGIST -> psychologist = null;
            }
        }
        else if (removedRole instanceof MafiaRole) {
            mafias.remove(removedRole);
            Roles roleName = removedRole.getRole();
            switch (roleName) {
                case MAFIA -> normalMafias.remove(removedRole);
                case DRLECTERE -> drLecter = null;
                case GODFATHER -> godFather = null;
            }
        }
        if (removedRole.die()){
            watchers.add(removedRole);
        }
        voted = removedRole;
        roles.remove(removedRole);
    }

    /**
     * checks if the game is finished
     *
     * @return true if finished,false if not
     * @throws IOException prints trace to find the problem
     */
    private boolean checkEndGame () throws IOException {
         if (mafias.size() == 0){
             endGame("City");
             return false;
         }
         else if (citizens.size() <= mafias.size()){
             endGame("Mafia");
             return false;
         }
         else {
             return true;
         }
    }

    /**
     * removes a role using its name
     *
     * @param name the name of role
     */
    public synchronized void removeRole(String name){
        Role removedRole = null;
        for (Role tempRole : roles){
            if (tempRole.getName().equals(name)){
                removedRole = tempRole;
                break;
            }
        }
        removeRole(removedRole);
    }

    /**
     * ends the game
     *
     * @param winners the winner group
     * @throws IOException prints trace to find the problem
     */
    private void endGame (String winners) throws IOException {
        sendToAll("0" + winners);
    }

    /**
     * randomizes a collection of roles
     *
     * @param random the random class object
     * @param roleArrayList the collection
     */
    private void randomizer (SecureRandom random,ArrayList<Role> roleArrayList){
        for (int i = 0 ; i < roleArrayList.size() ; i++){
            roleArrayList.add(getRandomRole(random,roleArrayList));
        }
    }

    /**
     * returns a random role from a collection
     *
     * @param random thr random classs object
     * @param roleArrayList the collection
     * @return the random role
     */
    private Role getRandomRole(SecureRandom random,ArrayList<Role> roleArrayList){
        int index = random.nextInt(roleArrayList.size());
        Role tempRole = roleArrayList.get(index);
        roleArrayList.remove(tempRole);
        return tempRole;
    }

    /**
     * sends a message to all members of game
     *
     * @param message the message
     * @throws IOException prints trace to find the problem
     */
    private void sendToAll (String message) throws IOException {
        for (Role role : roles) {
            role.getOutput().writeObject(message);
        }
        for (Role role : watchers) {
            role.getOutput().writeObject(message);
        }
    }

    /**
     * removes a watcher
     *
     * @param role the watcher
     */
    public void removeWatcher (Role role){
        watchers.remove(role);
    }
}
