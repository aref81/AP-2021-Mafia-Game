package com.company;

/**
 * exception that indicates a name exists
 * extend Exception
 *
 * @author Mohammad Hosein Aref
 * @version 1.0
 *
 */
public class NameExistException extends Exception{
    private String name;

    /**
     * initializes a exception with a name
     *
     * @param name the name
     */
    public NameExistException(String name) {
        this.name = name;
    }

    /**
     * returns the name
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * returns the exception message string
     *
     * @return the exception message string
     */
    @Override
    public String toString() {
        return "2NameExistException;\nthe user name " + name + " already exists\nplease try again\n";
    }
}
