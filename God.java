package com.company;

import java.io.IOException;
import java.util.ArrayList;

public class God {
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

    public God(ArrayList<Role> roles) {
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
    }

    public void firstNight (Role role) throws IOException, InterruptedException {
        if (role instanceof MafiaRole) {
            role.getOutput().writeObject("0\nFirst Night Begins!\nMAFIA group Wakes Up\n\nMAFIA members :\n");
            for (int i = 1; i <= mafias.size(); i++) {
                role.getOutput().writeObject("0" + i + "- " + mafias.get(i - 1).getRole() + " ---> " + mafias.get(i - 1).getName() + (mafias.get(i - 1) == role ? " (You)\n" : "\n"));
            }
            role.getOutput().writeObject("0\n\nMafia Group Sleeps.\n\n");
        }
        if (role instanceof CitizenRole) {
            role.getOutput().writeObject("0\nFirst Night Begins!\n");
            if (role.getRole() == Roles.MAYOR) {
                Thread.sleep(50);
                role.getOutput().writeObject("0\nMayor Wakes Up\n" + doctor.getRole() + " ---> " + doctor.getName() + "\n");
            } else if (role.getRole() == Roles.DOCTOR) {
                role.getOutput().writeObject("0\nDoctor Wakes Up\n" + mayor.getRole() + " ---> " + mayor.getName() + "\n");
            }
        }
        role.getOutput().writeObject("0Night Finishes.\n\n");
    }
}
