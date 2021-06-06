package com.company;

import java.io.IOException;
import java.net.Socket;

public abstract class CitizenRole extends Role{

    public CitizenRole(Socket socket,ClientsHandler clientsHandler,Roles role){
        super(socket,clientsHandler,role);
    }
}
