package com.company;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public enum Roles {
    CITIZEN,DOCTOR,DRLECTERE,GODFATHER,MAFIA,MAYOR,PROFESSIONAL,PSYCHOLOGIST,TOUGHLIFE,DETECTORE;

    public static CopyOnWriteArrayList<Roles> randomize(int num){
        CopyOnWriteArrayList<Roles> roles = new CopyOnWriteArrayList<Roles>();
        int mafia = (num - 8)/3;
        int citizen = num - 8 - mafia;

        roles.add(GODFATHER);
        roles.add(DOCTOR);
        roles.add(DRLECTERE);
        roles.add(MAYOR);
        roles.add(PROFESSIONAL);
        roles.add(PSYCHOLOGIST);
        roles.add(TOUGHLIFE);
        roles.add(DETECTORE);

        for (int i = 0;i < mafia;i++) {
            roles.add(MAFIA);
        }
        for (int i = 0;i < citizen;i++) {
            roles.add(CITIZEN);
        }

        for (int i = 0;i < num;i++){
            roles.add(getRandomRole(roles));
        }

        return roles;
    }

    private static Roles getRandomRole (CopyOnWriteArrayList<Roles> roles){
        Random random = new Random();
        int index = random.nextInt(roles.size());
        Roles tempRoles = roles.get(index);
        roles.remove(tempRoles);
        return  tempRoles;
    }
}
