package com.company;

public class NameExistException extends Exception{
    private String name;

    public NameExistException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "2NameExistException;\nthe user name " + name + " already exists\nplease try again\n";
    }
}
