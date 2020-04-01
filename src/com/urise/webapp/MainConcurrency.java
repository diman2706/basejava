package com.urise.webapp;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MainConcurrency {
    public static final Object LOCK1 = "lock1";
    public static final Object LOCK2 = "lock2";

    public static void main(String[] args) {
        deadLock(LOCK1, LOCK2);
        deadLock(LOCK2, LOCK1);
    }

    public static void deadLock(Object lock1, Object lock2) {
        new Thread(() -> {
            System.out.println("some thread waiting for " + lock1);
            synchronized (lock1) {
                System.out.println("some thread holding " + lock1);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("some thread waiting for " + lock2);
                synchronized (lock2) {
                    System.out.println("some thread holded lock1 and lock2");
                }
            }
        }).start();
    }
}
