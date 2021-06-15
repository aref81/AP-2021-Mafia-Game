package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * implements the main server of game
 * accepts and connects to clients
 * creates the client handler to start the game
 *
 * @author Mohammad Hosein Aref
 * @version 1.0
 *
 */
public class Server {
    /**
     * starts a server
     *
     * @param args
     * @throws IOException prints trace to find the problem
     * @throws InterruptedException prints trace to find the problem
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner= new Scanner(System.in);
        boolean loop = true;
        int num = 0;
        ServerSocket socket = null;
        while (loop) {
            try {
                System.out.println("Please Enter intended port  :");
                String port = scanner.nextLine();
                System.out.println("Please Enter number of players (at least 8):");
                num = scanner.nextInt();
                if (num < 8){
                    throw new IOException();
                }
                socket = new ServerSocket(Integer.parseInt(port));
                loop = false;
            } catch (SocketException e) {
                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                System.err.println("\nPort Unreachable!\nPlease try again.");
                scanner.nextLine();
            } catch (NumberFormatException | InputMismatchException e){
                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                System.err.println("\nInvalid Format Entered!\nenter in an integer number format");
                scanner.nextLine();
            }
            catch (IOException e){
                System.err.println("number of players lower than expected!");
                scanner.nextLine();
            }
        }
        ClientsHandler clients = new ClientsHandler(num);

        System.out.println("waiting for clients.");
        for (int i = 0 ; i < num; i++) {
            Socket client = socket.accept();
            System.out.println("Client number "+ (i+1) +" accepted!");
            clients.add(client);
        }
        socket.close();
        System.out.println("game finished closing server!");
    }
}
