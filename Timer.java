package com.company;

public class Timer {
    private int time;

    public Timer(int time) {
        this.time = time;
    }

    public boolean start() {
        try {
            Thread.sleep(60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
}
