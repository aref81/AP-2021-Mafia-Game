package com.company;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;

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

    private void gameLoop () throws IOException, InterruptedException {
        boolean loop = true;
        SecureRandom random = new SecureRandom();
        while (loop) {
            if (day) {
                for (Role role : roles) {
                    role.setReady(false);
                }
                sendToAll("0\nSun Rises.\nand Day begins!\n\n");

                if (isShot) {
                    if (shot != null) {
                        sendToAll("0Last night Mafia Shot " + shot.getName() + "\n");
                        removeRole(shot);
                        shot = null;
                    } else {
                        sendToAll("0Last night Mafia's bullet was missed!\n");
                    }
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
                    proShot = null;
                }
                if (req){
                    StringBuilder output = new StringBuilder("0Request for dead roles received\ndead roles :\n");
                    randomizer(random,deadRoles);
                    for (int i = 1; i <= deadRoles.size();i++){
                        output.append(i).append("-").append(deadRoles.get(i - 1).getName()).append("\n");
                    }
                    output.append("\n");

                    sendToAll(output.toString());
                    req = false;
                }

                if (muted != null){
                    sendToAll("0Psychologist has muted " + muted.getName() + "\n");
                    watchers.add(muted);
                    roles.remove(muted);
                }
                loop = checkEndGame();
                if (!loop){
                    break;
                }

                ChatServer chatServer = new ChatServer(roles,watchers);
                chatServer.startChat();
                PoolService poolService = new PoolService(roles,watchers);
                Role mayrem = poolService.StartPool();
                if (mayor.isAlive()){
                    sendToAll("0Mayor now makes the decision on veto.\n");
                    mayrem = mayor.veto(mayrem);
                }
                if (mayrem != null) {
                    removeRole(mayrem);
                }
                watchers.remove(muted);
                roles.add(muted);
                muted = null;
                day = false;
            } else {
                for (Role role : roles) {
                    role.setReady(false);
                }
                sendToAll("0\nSun Sets.\nand Night Begins!\n\n");
                sendToAll("0Mafia Group Wakes Up.");
                ArrayList<Role> mafiaChat = new ArrayList<>(mafias.size());
                mafiaChat.addAll(mafias);
                ChatServer chatServer = new ChatServer(mafiaChat,watchers);
                chatServer.startChat();
                PoolService poolService = new PoolService(citizens,mafias,watchers);
                shot = poolService.StartPool();

                if (godFather.isAlive()) {
                    for (Role role : mafias) {
                        role.getOutput().writeObject("0Pool Finished!\nGodFather now decides and takes the shot!");
                    }
                    shot = godFather.shoot(citizens);
                    for (Role role : mafias) {
                        role.getOutput().writeObject("0GodFather shot " + shot.getName() + "\n");
                    }
                }
                else {
                    for (Role role : mafias) {
                        role.getOutput().writeObject("0Pool Finished!\nnow Mafia take the shot!");
                    }
                }

                sendToAll("0Mafia Group Sleeps\n");

                Role savedMafia = null;
                sendToAll("0DrLecter Wakes Up!\n");

                if (doctor.isAlive()) {
                    savedMafia = drLecter.save(mafias);
                }
                else {
                    Thread.sleep(random.nextInt(5000));
                }
                sendToAll("0DrLecter Sleeps.\n");

                Role savedRole = null;
                sendToAll("0Doctor Wakes Up!\n");
                if (drLecter.isAlive()) {
                    savedRole = doctor.save(roles);
                }
                else {
                    Thread.sleep(random.nextInt(5000));
                }
                sendToAll("0Doctor Sleeps.\n");

                sendToAll("0Detector Wakes Up!\n");
                if (detector.isAlive()) {
                    detector.investigate(roles);
                }
                else {
                    Thread.sleep(random.nextInt(5000));
                }
                sendToAll("0Detector Sleeps.\n");

                sendToAll("0Professional Wakes Up!\n");
                if (professional.isAlive()) {
                    proShot = professional.Shoot(roles);
                }
                else {
                    Thread.sleep(random.nextInt(5000));
                }
                sendToAll("0Professional Sleeps.\n");

                sendToAll("0Psychologist Wakes Up!\n");
                if (psychologist.isAlive()) {
                    muted = psychologist.mute(roles);
                }
                else {
                    Thread.sleep(random.nextInt(5000));
                }
                sendToAll("0Psychologist Sleeps.\n");

                sendToAll("0Tough Life Wakes Up!\n");
                if (toughLife.canReq() && toughLife.isAlive()) {
                    req = toughLife.deadRolesRequest();
                } else {
                    Thread.sleep(random.nextInt(5000));
                }
                sendToAll("0Tough Life Sleeps.\n");

                if (savedMafia == proShot) {
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

    private void removeRole(Role removedRole){
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
    }

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

    private void endGame (String winners) throws IOException {
        for (Role role : roles) {
            role.getOutput().writeObject("0"+ winners);
        }
    }

    private void randomizer (SecureRandom random,ArrayList<Role> roleArrayList){
        for (int i = 0 ; i < roleArrayList.size() ; i++){
            roleArrayList.add(getRandomRole(random,roleArrayList));
        }
    }

    private Role getRandomRole(SecureRandom random,ArrayList<Role> roleArrayList){
        int index = random.nextInt(roleArrayList.size());
        Role tempRole = roleArrayList.get(index);
        roleArrayList.remove(tempRole);
        return tempRole;
    }

    private void sendToAll (String message) throws IOException {
        for (Role role : roles) {
            role.getOutput().writeObject(message);
        }
        for (Role role : watchers) {
            role.getOutput().writeObject(message);
        }
    }
}
