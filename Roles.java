package com.company;

import java.util.ArrayList;
import java.util.Random;

public enum Roles {
    CITIZEN,DOCTOR,DRLECTERE,GODFATHER,MAFIA,MAYOR,PROFESSIONAL,PSYCHOLOGIST,TOUGHLIFE,DETECTORE;

    public static ArrayList<Roles> randomize(){
        ArrayList<Roles> roles = new ArrayList<>(10);
        roles.add(GODFATHER);
        roles.add(DOCTOR);
        roles.add(DRLECTERE);
        roles.add(MAYOR);
        roles.add(PROFESSIONAL);
        roles.add(PSYCHOLOGIST);
        roles.add(TOUGHLIFE);
        roles.add(DETECTORE);
        roles.add(MAFIA);
        roles.add(CITIZEN);

        for (int i = 0;i < 10;i++){
            roles.add(getRandomRole(roles));
        }

        return roles;
    }

    private static Roles getRandomRole (ArrayList<Roles> roles){
        Random random = new Random();
        int index = random.nextInt(roles.size());
        Roles tempRoles = roles.get(index);
        roles.remove(tempRoles);
        return  tempRoles;
    }
}
