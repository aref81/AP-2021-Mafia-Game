package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class God implements Runnable{
    private ArrayList<Role> roles;
    private ArrayList<Role> mafias;
    private ArrayList<Role> citizens;
    private Role godFather;
    private Role detector;
    private Role doctor;
    private Role drLecter;
    private Role mayor;
    private Role professional;
    private Role psychologist;
    private Role toughLife;
    private ArrayList<Role> normalMafias;
    private ArrayList<Role> normalCitizens;
    private boolean day;
    private Boolean timer;

    public God(ArrayList<Role> roles) {
        this.roles = roles;
        mafias = new ArrayList<>(2);
        normalMafias = new ArrayList<>();

        for (Role role : roles){
            if (role instanceof MafiaRole){
                mafias.add(role);
            }
            Roles roleName = role.getRole();
            switch (roleName){
                case MAFIA -> normalMafias.add(role);
                case DRLECTERE -> drLecter = role;
                case GODFATHER -> godFather = role;
            }
        }

        citizens = new ArrayList<>(6);
        normalCitizens = new ArrayList<>();

        for (Role role : roles){
            if (role instanceof CitizenRole){
                citizens.add(role);
            }
            Roles roleName = role.getRole();
            switch (roleName){
                case MAYOR -> mayor =role;
                case DOCTOR -> doctor = role;
                case CITIZEN -> citizens.add(role);
                case DETECTORE -> detector = role;
                case TOUGHLIFE -> toughLife = role;
                case PROFESSIONAL -> professional = role;
                case PSYCHOLOGIST -> psychologist =role;
            }
        }

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
            role.getOutput().writeObject("0\nMAFIA group Wakes Up\n\nMAFIA members :\n");
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
            role.getOutput().writeObject("0Night Finishes.\n\n");
            role.setReady(false);
        }
        day = true;
    }

    public synchronized void gameLoop () throws IOException {
        ExecutorService pool = Executors.newCachedThreadPool();
        for (Role role : roles){

        }
    }

    public Boolean getTimer() {
        return timer;
    }
}
