package com.example.demo.showcase;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demonstrates concurrency features in Java
 * Including threads, volatile, ThreadLocal, synchronized, and concurrent utilities
 */
public class ConcurrencyShowcase {

    public static void demonstrate() {
        System.out.println("\n========== CONCURRENCY SHOWCASE ==========\n");

        basicThreading();
        volatileDemo();
        threadLocalDemo();
        synchronizedDemo();
        executorServiceDemo();
        atomicVariablesDemo();
    }

    // ========== Basic Threading ==========

    static class SimpleThread extends Thread {
        private String name;

        public SimpleThread(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 3; i++) {
                System.out.println(name + " - Count: " + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class RunnableTask implements Runnable {
        private String name;

        public RunnableTask(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 3; i++) {
                System.out.println(name + " - Task " + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void basicThreading() {
        System.out.println("--- Basic Threading ---");

        // Creating thread by extending Thread class
        SimpleThread thread1 = new SimpleThread("Thread-1");
        SimpleThread thread2 = new SimpleThread("Thread-2");

        thread1.start();
        thread2.start();

        // Creating thread using Runnable
        Thread thread3 = new Thread(new RunnableTask("Runnable-1"));
        thread3.start();

        // Using lambda (Java 8+)
        Thread thread4 = new Thread(() -> {
            for (int i = 1; i <= 3; i++) {
                System.out.println("Lambda-Thread - " + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread4.start();

        // Wait for all threads to complete
        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("All threads completed\n");
    }

    // ========== Volatile Keyword ==========

    static class VolatileExample {
        private volatile boolean flag = false;
        private int counter = 0;

        public void writer() {
            System.out.println("Writer: Setting flag to true");
            counter = 42;
            flag = true; // volatile write
        }

        public void reader() {
            while (!flag) {
                // Busy wait - volatile ensures visibility
            }
            System.out.println("Reader: Flag is true, counter = " + counter);
        }
    }

    private static void volatileDemo() {
        System.out.println("--- Volatile Keyword ---");
        System.out.println("volatile ensures visibility of changes across threads");

        VolatileExample example = new VolatileExample();

        Thread writerThread = new Thread(() -> {
            try {
                Thread.sleep(200);
                example.writer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread readerThread = new Thread(example::reader);

        readerThread.start();
        writerThread.start();

        try {
            writerThread.join();
            readerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println();
    }

    // ========== ThreadLocal ==========

    static class ThreadLocalExample {
        private static ThreadLocal<Integer> threadLocalValue = ThreadLocal.withInitial(() -> 0);

        public static void demonstrateThreadLocal() {
            System.out.println("--- ThreadLocal ---");
            System.out.println("ThreadLocal provides thread-specific values");

            Runnable task = () -> {
                String threadName = Thread.currentThread().getName();
                int value = (int) (Math.random() * 100);

                // Set thread-local value
                threadLocalValue.set(value);

                System.out.println(threadName + " set value: " + value);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Get thread-local value (same value set earlier)
                int retrievedValue = threadLocalValue.get();
                System.out.println(threadName + " retrieved value: " + retrievedValue);

                // Clean up
                threadLocalValue.remove();
            };

            Thread t1 = new Thread(task, "Thread-A");
            Thread t2 = new Thread(task, "Thread-B");
            Thread t3 = new Thread(task, "Thread-C");

            t1.start();
            t2.start();
            t3.start();

            try {
                t1.join();
                t2.join();
                t3.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println();
        }
    }

    private static void threadLocalDemo() {
        ThreadLocalExample.demonstrateThreadLocal();
    }

    // ========== Synchronized ==========

    static class Counter {
        private int count = 0;

        // Synchronized method
        public synchronized void increment() {
            count++;
        }

        // Synchronized block
        public void incrementBlock() {
            synchronized (this) {
                count++;
            }
        }

        public int getCount() {
            return count;
        }
    }

    private static void synchronizedDemo() {
        System.out.println("--- Synchronized Keyword ---");

        Counter counter = new Counter();
        int iterations = 1000;

        Runnable task = () -> {
            for (int i = 0; i < iterations; i++) {
                counter.increment();
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        Thread t3 = new Thread(task);

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Expected count: " + (iterations * 3));
        System.out.println("Actual count: " + counter.getCount());
        System.out.println("Synchronized ensures thread-safe operations\n");
    }

    // ========== ExecutorService ==========

    private static void executorServiceDemo() {
        System.out.println("--- ExecutorService ---");

        // Fixed thread pool
        ExecutorService executor = Executors.newFixedThreadPool(3);

        System.out.println("Submitting tasks to executor:");
        for (int i = 1; i <= 5; i++) {
            int taskId = i;
            executor.submit(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println("Task " + taskId + " running on " + threadName);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Task " + taskId + " completed");
            });
        }

        // Callable with Future
        System.out.println("\nUsing Callable with Future:");
        Callable<Integer> callableTask = () -> {
            Thread.sleep(100);
            return 42;
        };

        Future<Integer> future = executor.submit(callableTask);
        try {
            Integer result = future.get(); // Blocking call
            System.out.println("Callable result: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // Shutdown executor
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("ExecutorService shutdown complete\n");
    }

    // ========== Atomic Variables ==========

    private static void atomicVariablesDemo() {
        System.out.println("--- Atomic Variables ---");

        AtomicInteger atomicCounter = new AtomicInteger(0);
        int iterations = 1000;

        Runnable task = () -> {
            for (int i = 0; i < iterations; i++) {
                atomicCounter.incrementAndGet();
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        Thread t3 = new Thread(task);

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Expected count: " + (iterations * 3));
        System.out.println("Atomic count: " + atomicCounter.get());
        System.out.println("Atomic variables provide lock-free thread-safe operations");

        // Other atomic operations
        System.out.println("\nAtomic operations:");
        AtomicInteger num = new AtomicInteger(10);
        System.out.println("Initial: " + num.get());
        System.out.println("Get and increment: " + num.getAndIncrement());
        System.out.println("After increment: " + num.get());
        System.out.println("Add and get: " + num.addAndGet(5));
        System.out.println("Compare and set (15, 20): " + num.compareAndSet(15, 20));
        System.out.println("Final value: " + num.get());

        System.out.println();
    }
}
