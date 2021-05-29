package com.company;

import java.net.Socket;

public abstract class CitizenRole extends Role{

    public CitizenRole(Socket socket) {
        super(socket);
    }
}
