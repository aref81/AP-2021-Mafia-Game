package com.company;

import java.net.Socket;

public abstract class Role {
    protected boolean alive;
    protected String name;
    protected Socket socket;

    public Role (Socket socket) {
        alive = true;
        this.socket = socket;
    }

}
