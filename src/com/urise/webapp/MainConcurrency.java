package com.urise.webapp;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MainConcurrency {
    public static final Object LOCK1 = new Object();
    public static final Object LOCK2 = new Object();

    public static void main(String[] args) {
        TestOne testOne = new TestOne();
        TestTwo testTwo = new TestTwo();
        testOne.start();
        testTwo.start();
    }

    private static class TestOne extends Thread{
        @Override
        public void run() {
            synchronized (LOCK1){
                System.out.println("testOne hold lock1 ");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("testOne waiting for lock2");
                synchronized (LOCK2){
                    System.out.println("testOne holded lock1 and lock2");
                }
            }
        }
    }
    private static class TestTwo extends Thread{
        @Override
        public void run() {
            synchronized (LOCK2){
                System.out.println("testTwo hold lock2 ");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("testTwo waiting for lock1");
                synchronized (LOCK1){
                    System.out.println("testTwo holded lock1 and lock2");
                }
            }
        }
    }
}
