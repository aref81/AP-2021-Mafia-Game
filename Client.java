package com.company;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
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
                chatRoomMode(output,input);
            }
            else if (command.equals("quit")){
                quit(output);
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

    private static void chatRoomMode (ObjectOutputStream output,ObjectInputStream input) throws IOException {
        System.out.println("***CHAT ROOM***");
        ExecutorService pool = Executors.newCachedThreadPool();
        ReaderThread readerThread = new ReaderThread(input);
        WriterThread writerThread = new WriterThread(output,readerThread);
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
        readerThread.interrupt();
    }
    private static void quit (ObjectOutputStream output) throws IOException, InterruptedException {
        Thread t = new quit(output);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (t.isAlive())
                System.out.println("Time's Up\nQuiting!\n");
                try {
                    output.writeObject(1);
                    t.interrupt();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        },10 * 1000);
        while (t.isAlive()){
            Thread.sleep(1000);
        }
    }
}

class quit extends Thread {
    ObjectOutputStream output;
    Scanner scanner;

    public quit(ObjectOutputStream output) {
        this.output = output;
        scanner = new Scanner(System.in);
    }

    @Override
    public void run() {
        System.out.println("1Do you want to:\n(1) quit\nor\n(2)continue watching the game?(you are unable to talk to alive people)\nyou have 10 seconds to answer");
        try {
            output.writeObject(scanner.nextInt());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class ReaderThread extends Thread{
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
                System.out.println("\033[1A\033[" + "\r" + message);
                message = (String) input.readObject();
            }

            System.out.println("\rChat time is over!\n");
            ended = true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean isEnded() {
        return ended;
    }
}

class WriterThread extends Thread{
    private ObjectOutputStream output;
    private Scanner scanner;
    private ReaderThread readerThread;
    private boolean end;

    public WriterThread(ObjectOutputStream output,ReaderThread readerThread) {
        this.output = output;
        scanner = new Scanner(System.in);
        this.readerThread = readerThread;
        end = false;
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
        end = true;
    }

    public boolean isEnd() {
        return end;
    }
}
