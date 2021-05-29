package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main (String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please Enter the port which tou want to connect to :\n");
        String port;
        boolean loop;
        do {
            loop = false;
            port = scanner.nextLine();
            for (char c : port.toCharArray()){
                if (!Character.isDigit(c)){
                    loop = true;
                    break;
                }
            }

        }while (loop);

        Socket socket = new Socket("127.0.0.1",Integer.parseInt(port));
        OutputStream outputStream = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();

    }
}
