package com.company;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {



    public static void main (String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        Socket socket = null;

//        boolean loop = true;
//        do {
//            try {
//                System.out.println("Please enter the ip you want to connect to :");
//                String ip = scanner.nextLine();
//                System.out.println("Please enter the destination port :");
//                String port = scanner.nextLine();
//
//                socket = new Socket(ip, Integer.parseInt(port));
//                loop = false;
//                System.out.println("Successfully connected to Mafia Server");
//            } catch (SocketException e) {
//                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
//                System.err.println("\nSocket Unreachable!\nPlease try again.\n");
//            } catch (NumberFormatException e){
//                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
//                System.err.println("\nInvalid Format Entered!\nport is an integer number format\n");
//            } catch (UnknownHostException e){
//                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
//                System.err.println("\nInvalid ip!\nip is in xxx.xxx.xxx.xxx format\nplease check your ip and try again\n");
//            }
//        }while (loop);

        socket = new Socket("127.0.0.1", 8585);

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
            else if (command.equals("chatMode")){
                chatRoomMode(output,input,scanner);
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

    private static void chatRoomMode (ObjectOutputStream output,ObjectInputStream input,Scanner scanner){
        System.out.println("***CHAT ROOM***");
        ExecutorService pool = Executors.newCachedThreadPool();
        ReaderThread readerThread = new ReaderThread(input);
        WriterThread writerThread = new WriterThread(output,scanner,readerThread);
        pool.execute(readerThread);
        pool.execute(writerThread);
        while (!readerThread.isEnded()){
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}

class ReaderThread implements Runnable{
    private ObjectInputStream input;
    private boolean ended;

    public ReaderThread(ObjectInputStream input) {
        this.input = input;
        ended = false;
    }

    @Override
    public void run() {
        try {
            String message = (String) input.readObject();

            while (!(message.equals("exitchatMode"))){
                System.out.println(message);
                message = (String) input.readObject();
            }

            System.out.println("Chat time is over!\n");
            ended = true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean isEnded() {
        return ended;
    }
}

class WriterThread implements Runnable{
    private ObjectOutputStream output;
    private Scanner scanner;
    private ReaderThread readerThread;

    public WriterThread(ObjectOutputStream output,Scanner scanner,ReaderThread readerThread) {
        this.output = output;
        this.scanner = scanner;
        this.readerThread = readerThread;
    }

    @Override
    public void run() {
        while (!readerThread.isEnded()){
            String message = scanner.nextLine();
            try {
                output.writeObject(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
