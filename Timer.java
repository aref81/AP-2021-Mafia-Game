package com.company;

public class Timer implements Runnable{
    private int time;
    private Boolean timer;

    public Timer(int time,Boolean timer) {
        this.time = time;
        this.timer = timer;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(60 * 1000);
            timer = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
