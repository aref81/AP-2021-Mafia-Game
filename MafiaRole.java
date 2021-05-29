package com.company;

import java.net.Socket;

public abstract class MafiaRole extends Role{

    public MafiaRole(Socket socket) {
        super(socket);
    }
}
