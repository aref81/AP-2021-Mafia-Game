package com.company;


import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * implements the client of the game
 *
 * contains methods and classes to control client side
 * connects and receives and sends commands to server
 * through sockets
 *
 * @author Mohammad Hosein Aref
 * @version 1.0
 *
 */
public class Client {


    /**
     * starts a client program
     *
     * @param args
     * @throws IOException prints trace to find the problem
     * @throws ClassNotFoundException prints trace to find the problem
     * @throws InterruptedException prints trace to find the problem
     */
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

        socket = new Socket("127.0.0.1", 8585);

        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());


        while (!socket.isClosed()){
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
            else if (command.equals("pool")){
                pool(output,input);
            }
            else if (command.charAt(0) == '1') {
                System.out.print(command.substring(1));
                String str= scanner.nextLine();
                output.writeObject(str);
            }
            else if (command.charAt(0) == '2'){
                System.err.println(command.substring(1));
            }
            else if (command.charAt(0) == '3'){
                String str = "";
                for (int i = 0; i < 2 ; i++){
                    str += command.charAt(i+1);
                }
                int border = Integer.parseInt(str);
                int index = -1;
                System.out.print(command.substring(3));
                while (index == -1){
                    String out = scanner.nextLine();
                    if (!isDigit(out)){
                        System.err.println("Invalid Type of input!");
                        continue;
                    }
                    index = Integer.parseInt(out);
                    if (index > border || index < 1){
                        System.err.println("out of border!");
                        index = -1;
                        continue;
                    }
                    output.writeObject(out);
                }
            }
        }
        socket.close();

        System.out.println("Exited from game\n");
    }

    /**
     * sets client to poolMode
     *
     * @param output output of client
     * @param input input of client
     * @throws InterruptedException prints trace to find the problem
     * @throws IOException prints trace to find the problem
     */
    private static void pool (ObjectOutputStream output,ObjectInputStream input) throws InterruptedException, IOException {
        Pool poolClient = new Pool(output,input);
        ExecutorService pool = Executors.newCachedThreadPool();

        pool.execute(poolClient);
        pool.shutdown();

        for (int i = 0 ; (i < 30) && (!poolClient.isRespond());i++){
            Thread.sleep(1000);
        }

        if (!poolClient.isRespond()){
            System.out.println("You voted no one!\nPlease press \"Enter\" Whenever you are ready.\n");
            while (!pool.isTerminated()){
                Thread.sleep(1000);
            }
            output.writeObject("0");
        }
    }

    /**
     * set client to chat mode
     *
     * @param output output of client
     * @param input input of client
     */
    private static void chatRoomMode (ObjectOutputStream output,ObjectInputStream input) {
        System.out.println("***CHAT ROOM***");
        ExecutorService pool = Executors.newCachedThreadPool();
        ReaderThread readerThread = new ReaderThread(input);
        WriterThread writerThread = new WriterThread(output,readerThread);
        pool.execute(readerThread);
        pool.execute(writerThread);
        pool.shutdown();
        while (!writerThread.isEnd()){
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e){
                System.err.println("An error occurred while sing chatroom!\nleaving the game...");
            }
        }
    }

    /**
     * set client in quest of quit
     *
     * @param output output of client
     * @throws InterruptedException prints trace to find the problem
     * @throws IOException prints trace to find the problem
     */
    private static void quit (ObjectOutputStream output) throws InterruptedException, IOException {
        ExecutorService pool = Executors.newCachedThreadPool();
        Quit quit = new Quit(output);
        pool.execute(quit);
        pool.shutdown();


        for (int i = 0 ; (i < 10) && (!quit.getRespond());i++){
            Thread.sleep(1000);
        }

        if (!quit.getRespond()){
            output.writeObject("1");
            System.out.println("Time's Up\nQuitting!\nPlease press \"Enter\" to terminate execution.");
        }
    }

    /**
     * checks if a string is a digit or no
     *
     * @param str the string
     * @return true if digit, fasle if not
     */
    private static boolean isDigit(String str){
        for (int i = 0; i < str.length() ; i++){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }
}


/**
 *
 * implements thread for pool mode
 *  implements runnable to multi thread
 */
class Pool implements Runnable{
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Scanner scanner;
    private boolean respond;

    /**
     * initializes a pool obj
     *
     * @param output output of client
     * @param input input of client
     */
    public Pool(ObjectOutputStream output,ObjectInputStream input) {
        this.input = input;
        this.output = output;
        scanner = new Scanner(System.in);
        respond = false;
    }

    /**
     * runs the pool thread
     *
     */
    @Override
    public void run(){
        try {
            System.out.println(input.readObject());
            String pool = scanner.nextLine();
            if ((!pool.equals("")) && isDigit(pool)) {
                output.writeObject(pool);
            }
            respond = true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * checks if a string is a digit or no
     *
     * @param str the string
     * @return true if digit, fasle if not
     */
    private boolean isDigit(String str){
        for (int i = 0; i < str.length() ; i++){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    /**
     * checks if the client is responded in time or not
     *
     * @return true if responded, false if not
     */
    public boolean isRespond() {
        return respond;
    }
}

/**
 *
 * implements thread for quit mode
 *  implements runnable to multi thread
 */
class Quit implements Runnable {
    private ObjectOutputStream output;
    private Scanner scanner;
    private Boolean respond;
    /**
     * initializes a quit obj
     *
     * @param output output of client
     */
    public Quit(ObjectOutputStream output) {
        respond = false;
        this.output = output;
        scanner = new Scanner(System.in);
    }
    /**
     * runs the quit thread
     *
     */
    @Override
    public void run() {
        System.out.println("1Do you want to:\n(1) quit\nor\n(2)continue watching the game?(you are unable to talk to alive people)\nyou have 10 seconds to answer");
        try {
            String choice = scanner.nextLine();
            if ((!choice.equals("")) && isDigit(choice)) {
                output.writeObject(choice);
                respond = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * checks if a string is a digit or no
     *
     * @param str the string
     * @return true if digit, fasle if not
     */
    private boolean isDigit(String str){
        for (int i = 0; i < str.length() ; i++){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }
    /**
     * checks if the client is responded in time or not
     *
     * @return true if responded, false if not
     */
    public Boolean getRespond() {
        return respond;
    }
}

/**
 * implements one of threads in chat mode
 * it reads messages and displays them
 * for client
 *
 * implements Runnable for multi Threading
 *
 */
class ReaderThread implements Runnable{
    private ObjectInputStream input;
    private boolean ended;

    /**
     * initializes a reader thread
     *
     * @param input input of client
     */
    public ReaderThread(ObjectInputStream input) {
        this.input = input;
        ended = false;
    }

    /**
     *
     * starts a reader thread
     */
    @Override
    public void run() {
        try {
            String message = (String) input.readObject();
            while (!(message.equals("exitchatMode"))){
                System.out.print("\r" + message + "\nYou :");
                message = (String) input.readObject();
            }

            System.out.println("\rChat time is over!\nPlease press \"Enter\" whenever you are ready.");
            ended = true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * returns the status of thread
     *
     * @return the status
     */
    public boolean isEnded() {
        return ended;
    }
}

/**
 * implements one of threads in chat mode
 * it scans message from client
 * and sends the to server
 *
 * implements Runnable for multi Threading
 *
 */
class WriterThread implements Runnable{
    private ObjectOutputStream output;
    private Scanner scanner;
    private ReaderThread readerThread;
    private boolean end;

    /**
     * initializes a writer thread
     *
     * @param output output of client
     * @param readerThread the reader thread in chat mode
     */
    public WriterThread(ObjectOutputStream output,ReaderThread readerThread) {
        this.output = output;
        scanner = new Scanner(System.in);
        this.readerThread = readerThread;
        end = false;
    }

    /**
     * starts the writer thread
     *
     */
    @Override
    public void run() {
        while (!readerThread.isEnded()) {
            System.out.print("You :");
            String message = scanner.nextLine();
            if (!message.equals("exit")) {
                try {
                    output.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            end = true;
        }
    }

    /**
     * returns the status of thread
     *
     * @return the status
     */
    public boolean isEnd() {
        return end;
    }
}
