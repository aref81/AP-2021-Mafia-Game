package com.company;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {



    public static void main (String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        Socket socket = null;

        boolean loop = true;
        do {
            try {
                System.out.println("Please enter the ip you want to connect to :");
                String ip = scanner.nextLine();
                System.out.println("Please enter the destination port :");
                String port = scanner.nextLine();

                socket = new Socket(ip, Integer.parseInt(port));
                loop = false;
                System.out.println("Successfully connected to Mafia Server");
            } catch (SocketException e) {
                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                System.err.println("\nSocket Unreachable!\nPlease try again.\n");
            } catch (NumberFormatException e){
                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                System.err.println("\nInvalid Format Entered!\nport is an integer number format\n");
            } catch (UnknownHostException e){
                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                System.err.println("\nInvalid ip!\nip is in xxx.xxx.xxx.xxx format\nplease check your ip and try again\n");
            }
        }while (loop);

        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());



        while (true){
            String command = (String) input.readObject();
            if (command.equals("exit")){
                break;
            }
            if (command.charAt(0) == '0'){
                System.out.print(command.substring(1));
            }
            else if (command.charAt(0) == '1') {
                System.out.print(command.substring(1));
                output.writeObject(scanner.nextLine());
            }
            else if (command.charAt(0) == '2'){
                System.err.println(command.substring(1));
            }
        }
        socket.close();

        System.out.println("Exited from game\n");


    }

}
