package com.company;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * enum for roles and contains
 * methods to genertae a random
 * collection of roles
 *
 * @author Mohammad Hosein Aref
 * @version 1.0
 *
 */
public enum Roles {
    CITIZEN,DOCTOR,DRLECTERE,GODFATHER,MAFIA,MAYOR,PROFESSIONAL,PSYCHOLOGIST,TOUGHLIFE,DETECTORE;

    /**
     * generate a random collection of roles
     *
     * @param num number intended
     * @return a random collection of roles
     */
    public static CopyOnWriteArrayList<Roles> randomize(int num){
        CopyOnWriteArrayList<Roles> roles = new CopyOnWriteArrayList<Roles>();
        int mafia = (num - 6)/3;
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

    /**
     * returns a random roleName from a collection
     *
     * @param roles the collection
     * @return the random roleName
     */
    private static Roles getRandomRole (CopyOnWriteArrayList<Roles> roles){
        Random random = new Random();
        int index = random.nextInt(roles.size());
        Roles tempRoles = roles.get(index);
        roles.remove(tempRoles);
        return  tempRoles;
    }
}
