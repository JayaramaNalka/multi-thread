package com.jayarama.multithread;

import java.util.ArrayList;
import java.util.List;

public class ThreadSynchronization {
    protected static int counter = 1;
    protected static final int maxThreads = 3;

    public static void main(String[] args) {
        List<Object> objects = new ArrayList<>();
        for (int i = 0; i < maxThreads; i++) {
            objects.add(new Object());
        }
        Object currentObject = objects.get(maxThreads - 1);
        for (int i = 0; i < maxThreads; i++) {
            final Object nextObject = objects.get(i);
            RunnableClass runnableClass = new RunnableClass(
                    currentObject,
                    nextObject,
                    i == 0 ? true : false
            );
            Thread thread = new Thread(runnableClass);
            thread.setName("Thread - " + (i + 1));
            thread.start();
            currentObject = objects.get(i);
        }
    }
}

class RunnableClass implements Runnable {
    private Object currentObject;
    private Object nextObject;
    private boolean first;

    public RunnableClass(Object currentObject,
                         Object nextObject,
                         boolean first) {
        this.currentObject = currentObject;
        this.nextObject = nextObject;
        this.first = first;
    }

    @Override
    public void run() {
        int i = 0;
        try {
            if (first) {
                Thread.sleep(5000);
                first = false;
                System.out.println(Thread.currentThread().getName() + " - " + ThreadSynchronization.counter++);
                synchronized (nextObject) {
                    nextObject.notify();
                }
            }
            while (i++ < 20) {
                synchronized (currentObject) {
                    currentObject.wait();
                }
                System.out.println(Thread.currentThread().getName() + " - " + ThreadSynchronization.counter++);
                Thread.sleep(1000);
                synchronized (nextObject) {
                    nextObject.notify();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}