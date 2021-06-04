package com.company;

import java.io.IOException;
import java.net.Socket;

public abstract class MafiaRole extends Role{

    public MafiaRole(Socket socket,ClientsHandler clientsHandler) throws IOException, ClassNotFoundException {
        super(socket,clientsHandler);
    }
}
