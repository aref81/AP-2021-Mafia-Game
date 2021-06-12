package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private Role shot;
    private Role voted;
    private boolean isShot;
    private boolean day;
    private Boolean timer;

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
        shot = null;
        voted = null;
        isShot = false;
        day = false;
        timer = false;
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

    private void gameLoop () throws IOException {
        if (day) {
            for (Role role : roles) {
                role.getOutput().writeObject("0\nNight Finishes.\nand Day begins!\n\n");
                role.setReady(false);
            }

            if (isShot){
                if (shot != null){
                    for (Role role : roles){
                        role.getOutput().writeObject("0Last night Mafia Shot " + shot.getName() + "\n");
                        removeRole(shot);
                    }
                }
                else {
                    for (Role role : roles){
                        role.getOutput().writeObject("0Last night Mafia's bullet was missed!\n");
                    }
                }
            }
            if (voted != null){
                for (Role role : roles){
                    if (voted instanceof CitizenRole) {
                        role.getOutput().writeObject("0Yesterday, we lost a citizen\n");
                    }
                    else {
                        role.getOutput().writeObject("0Yesterday, we executed a Mafia\n");
                    }
                }
            }
            ChatServer chatServer = new ChatServer(roles);
            chatServer.startChat();
            PoolService poolService= new PoolService(roles);
            removeRole(poolService.StartPool());
            day = false;
        }
        else {
            for (Role role : roles) {
                role.getOutput().writeObject("0\nSun Sets.\nand Night Begins!\n\n");
                role.setReady(false);
            }
            for (Role role : mafias){
                role.getOutput().writeObject("0Mafia Group Wakes Up.");
            }
            ArrayList<Role> mafiaChat = new ArrayList<>(mafias.size());
            mafiaChat.addAll(mafias);
            ChatServer chatServer = new ChatServer(mafiaChat);
            chatServer.startChat();
            for (Role role : mafias){
                role.getOutput().writeObject("0GodFather now takes the shot!");
            }
            shot = godFather.shoot(citizens);
            for (Role role : mafias){
                role.getOutput().writeObject("0GodFather shot " + shot.getName() + "\n" + "Mafia Group Sleeps\n");
            }

            drLecter.getOutput().writeObject("0DrLecter Wakes Up!\n");
            Role savedMafia = drLecter.save(mafias);
            drLecter.getOutput().writeObject("0DrLecter Sleeps.\n");

            doctor.getOutput().writeObject("0Doctor Wakes Up!\n");
            Role SavedRole = doctor.save(roles);
            doctor.getOutput().writeObject("0Doctor Sleeps.\n");

            detector.getOutput().writeObject("0Detector Wakes Up!\n");
            detector.investigate(roles);
            detector.getOutput().writeObject("0Detector Sleeps.\n");

        }
    }

    public Boolean getTimer() {
        return timer;
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
        removedRole.die();
        voted = removedRole;
    }
}
