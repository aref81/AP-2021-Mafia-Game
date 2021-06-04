package com.company;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main (String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please Enter the port which tou want to connect to :");
        String port;
        boolean loop;
        do {
            loop = false;
            port = scanner.nextLine();
            for (char c : port.toCharArray()) {
                if (!Character.isDigit(c)) {
                    loop = true;
                    System.out.println("Invalid entered port!\nPlease enter the port again :");
                    break;
                }
            }

        } while (loop);

        Socket socket = new Socket("127.0.0.1", Integer.parseInt(port));
        OutputStream outputStream = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();
        ObjectOutputStream output = new ObjectOutputStream(outputStream);
        ObjectInputStream input = new ObjectInputStream(inputStream);

        System.out.println("Please Enter Your User Name :");
        output.writeObject(scanner.nextLine());
        while (!((Boolean) input.readObject())) {
            System.out.println("user name already taken!\nPlease enter another one :");
            output.writeObject(scanner.nextLine());
        }
        System.out.println(input.readObject());

        loop = true;
        while (loop)
            try {
                String str = (String) input.readObject();
                if (str != null) {
                    System.out.println(str);
                    loop = false;
                }
            } catch (IOException | ClassNotFoundException e){
                Thread.sleep(2000);
                System.out.println("Still Waiting...");
    }
        Roles role = (Roles) input.readObject();
        System.out.println("You Are " + role + "!");
    }
}
