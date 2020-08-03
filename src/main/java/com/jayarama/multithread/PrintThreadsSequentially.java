package com.jayarama.multithread;

import java.util.ArrayList;
import java.util.List;

public class PrintThreadsSequentially {
    static int counter = 0;
    static final int maxThreads = 4;
    static final int PRINT_NUMBERS_UP_TO = 20;
    static Object lock = new Object();

    public static void main(String[] args) {
        List<SequentialThread> totalThreads = new ArrayList<>();
        for (int i = 1; i <= maxThreads; i++) {
            totalThreads.add(new SequentialThread("Thread - " + i, (i - 1)));
        }
        totalThreads.stream().forEach(SequentialThread::start);
    }

    static class SequentialThread extends Thread {
        int remainder = 0;

        public SequentialThread(String name, int remainder) {
            super(name);
            this.remainder = remainder;
        }

        @Override
        public void run() {
            while (counter < PRINT_NUMBERS_UP_TO) {
                synchronized (lock) {
                    while (counter % maxThreads != remainder) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (++counter > PRINT_NUMBERS_UP_TO)
                        break;
                    System.out.println(getName() + " value " + counter);

                    lock.notifyAll();
                }
            }
        }
    }
}
