package com.company;

public class Timer {
    private int time;

    public Timer(int time) {
        this.time = time;
    }

    public boolean start() {
        try {
            Thread.sleep(time * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
}
