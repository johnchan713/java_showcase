package com.example.demo.showcase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demonstrates ThreadPool and Executor framework in detail
 * Covers ExecutorService types, ThreadPoolExecutor configuration, and scheduling
 */
public class ThreadPoolExecutorShowcase {

    public static void demonstrate() {
        System.out.println("\n========== THREAD POOL & EXECUTOR SHOWCASE ==========\n");

        executorTypesDemo();
        threadPoolExecutorDemo();
        threadPoolConfigurationDemo();
        scheduledExecutorDemo();
        completionServiceDemo();
        executorShutdownDemo();
        rejectionPolicyDemo();
        threadFactoryDemo();
    }

    // ========== Executor Types ==========

    private static void executorTypesDemo() {
        System.out.println("--- Executor Types ---");
        System.out.println("Different executor implementations and their use cases\n");

        // 1. Single Thread Executor
        System.out.println("1. Single Thread Executor:");
        System.out.println("   ✓ Guarantees sequential execution");
        System.out.println("   ✓ Good for tasks that must run in order");
        System.out.println("   ✗ No parallelism");
        ExecutorService singleExecutor = Executors.newSingleThreadExecutor();
        for (int i = 1; i <= 3; i++) {
            final int taskNum = i;
            singleExecutor.submit(() -> System.out.println("   Single Thread Task " + taskNum + " - " + Thread.currentThread().getName()));
        }
        singleExecutor.shutdown();

        // 2. Fixed Thread Pool
        System.out.println("\n2. Fixed Thread Pool (3 threads):");
        System.out.println("   ✓ Bounded parallelism");
        System.out.println("   ✓ Reuses threads");
        System.out.println("   ✓ Good for CPU-bound tasks");
        ExecutorService fixedExecutor = Executors.newFixedThreadPool(3);
        for (int i = 1; i <= 6; i++) {
            final int taskNum = i;
            fixedExecutor.submit(() -> System.out.println("   Fixed Pool Task " + taskNum + " - " + Thread.currentThread().getName()));
        }
        fixedExecutor.shutdown();

        // 3. Cached Thread Pool
        System.out.println("\n3. Cached Thread Pool:");
        System.out.println("   ✓ Creates threads on demand");
        System.out.println("   ✓ Reuses idle threads (60s timeout)");
        System.out.println("   ✗ Can create too many threads");
        System.out.println("   USE: Short-lived async tasks, I/O bound");
        ExecutorService cachedExecutor = Executors.newCachedThreadPool();
        for (int i = 1; i <= 3; i++) {
            final int taskNum = i;
            cachedExecutor.submit(() -> System.out.println("   Cached Pool Task " + taskNum + " - " + Thread.currentThread().getName()));
        }
        cachedExecutor.shutdown();

        // 4. Work Stealing Pool (ForkJoinPool)
        System.out.println("\n4. Work Stealing Pool (ForkJoinPool):");
        System.out.println("   ✓ Uses work-stealing algorithm");
        System.out.println("   ✓ Great for recursive tasks");
        System.out.println("   ✓ Defaults to number of processors");
        System.out.println("   Processors: " + Runtime.getRuntime().availableProcessors());
        ExecutorService workStealingExecutor = Executors.newWorkStealingPool();
        for (int i = 1; i <= 3; i++) {
            final int taskNum = i;
            workStealingExecutor.submit(() -> System.out.println("   Work Stealing Task " + taskNum + " - " + Thread.currentThread().getName()));
        }
        workStealingExecutor.shutdown();

        waitForTermination(singleExecutor, fixedExecutor, cachedExecutor, workStealingExecutor);
        System.out.println();
    }

    // ========== ThreadPoolExecutor Details ==========

    private static void threadPoolExecutorDemo() {
        System.out.println("--- ThreadPoolExecutor Configuration ---");
        System.out.println("Manual configuration with all parameters\n");

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            2,                                      // corePoolSize: minimum threads
            4,                                      // maximumPoolSize: max threads
            60L,                                    // keepAliveTime: idle thread timeout
            TimeUnit.SECONDS,                       // time unit for keepAliveTime
            new LinkedBlockingQueue<>(10),          // workQueue: task queue (bounded)
            Executors.defaultThreadFactory(),       // threadFactory
            new ThreadPoolExecutor.CallerRunsPolicy() // rejectionHandler
        );

        System.out.println("ThreadPoolExecutor created:");
        System.out.println("  Core pool size: " + executor.getCorePoolSize());
        System.out.println("  Maximum pool size: " + executor.getMaximumPoolSize());
        System.out.println("  Keep alive time: " + executor.getKeepAliveTime(TimeUnit.SECONDS) + "s");
        System.out.println("  Queue capacity: 10 (bounded)");
        System.out.println("  Rejection policy: CallerRunsPolicy");

        // Submit tasks
        for (int i = 1; i <= 8; i++) {
            final int taskNum = i;
            executor.submit(() -> {
                System.out.println("  Task " + taskNum + " executing on " + Thread.currentThread().getName());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // Monitor pool
        System.out.println("\nPool status:");
        System.out.println("  Active threads: " + executor.getActiveCount());
        System.out.println("  Pool size: " + executor.getPoolSize());
        System.out.println("  Queue size: " + executor.getQueue().size());
        System.out.println("  Completed tasks: " + executor.getCompletedTaskCount());

        executor.shutdown();
        waitForTermination(executor);
        System.out.println();
    }

    // ========== Thread Pool Configuration Details ==========

    private static void threadPoolConfigurationDemo() {
        System.out.println("--- Thread Pool Configuration Parameters ---");
        System.out.println("Understanding corePoolSize, maximumPoolSize, and queues\n");

        // Queue types
        System.out.println("1. Queue Types:");
        System.out.println("   a) SynchronousQueue - Direct handoff (no capacity)");
        System.out.println("      ✓ No task buffering");
        System.out.println("      ✓ Creates threads up to max immediately");
        System.out.println("      USE: newCachedThreadPool()");

        System.out.println("\n   b) LinkedBlockingQueue - Unbounded queue");
        System.out.println("      ✓ Unlimited capacity");
        System.out.println("      ✗ Never creates threads beyond core size");
        System.out.println("      ✗ Risk of memory issues");
        System.out.println("      USE: newFixedThreadPool()");

        System.out.println("\n   c) ArrayBlockingQueue - Bounded queue");
        System.out.println("      ✓ Fixed capacity");
        System.out.println("      ✓ Controlled memory usage");
        System.out.println("      ✓ Creates threads when queue is full");
        System.out.println("      USE: Custom pools with backpressure");

        System.out.println("\n   d) PriorityBlockingQueue - Priority queue");
        System.out.println("      ✓ Tasks processed by priority");
        System.out.println("      ✓ Unbounded");
        System.out.println("      USE: Task prioritization");

        // Execution flow
        System.out.println("\n2. Task Execution Flow:");
        System.out.println("   Step 1: If threads < corePoolSize → Create new thread");
        System.out.println("   Step 2: If queue not full → Queue the task");
        System.out.println("   Step 3: If queue full and threads < maxPoolSize → Create new thread");
        System.out.println("   Step 4: If queue full and threads = maxPoolSize → Apply rejection policy");

        // Example with bounded queue
        System.out.println("\n3. Example with ArrayBlockingQueue:");
        ThreadPoolExecutor boundedExecutor = new ThreadPoolExecutor(
            2, 5, 60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(3)
        );

        System.out.println("   Configuration: core=2, max=5, queue=3");
        System.out.println("   Task 1-2: Create 2 core threads");
        System.out.println("   Task 3-5: Queue (3 slots)");
        System.out.println("   Task 6-8: Create 3 more threads (up to max=5)");
        System.out.println("   Task 9+: Reject (queue full, max threads reached)");

        boundedExecutor.shutdown();
        System.out.println();
    }

    // ========== Scheduled Executor ==========

    private static void scheduledExecutorDemo() {
        System.out.println("--- Scheduled Executor Service ---");
        System.out.println("Delayed and periodic task execution\n");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        // 1. Schedule with delay
        System.out.println("1. Schedule with delay (1 second):");
        scheduler.schedule(() ->
            System.out.println("   Delayed task executed at " + System.currentTimeMillis()),
            1, TimeUnit.SECONDS
        );

        // 2. Schedule at fixed rate
        System.out.println("\n2. Schedule at fixed rate (every 500ms, 3 times):");
        AtomicInteger counter1 = new AtomicInteger(0);
        ScheduledFuture<?> fixedRateFuture = scheduler.scheduleAtFixedRate(() -> {
            int count = counter1.incrementAndGet();
            System.out.println("   Fixed rate task #" + count + " at " + System.currentTimeMillis());
            if (count >= 3) {
                throw new RuntimeException("Stop"); // Will stop the task
            }
        }, 0, 500, TimeUnit.MILLISECONDS);

        // 3. Schedule with fixed delay
        System.out.println("\n3. Schedule with fixed delay (500ms delay between executions, 3 times):");
        AtomicInteger counter2 = new AtomicInteger(0);
        ScheduledFuture<?> fixedDelayFuture = scheduler.scheduleWithFixedDelay(() -> {
            int count = counter2.incrementAndGet();
            System.out.println("   Fixed delay task #" + count + " at " + System.currentTimeMillis());
            try {
                Thread.sleep(200); // Task takes 200ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (count >= 3) {
                throw new RuntimeException("Stop");
            }
        }, 0, 500, TimeUnit.MILLISECONDS);

        // 4. Callable with schedule (returns value)
        System.out.println("\n4. Scheduled Callable (returns value after 1 second):");
        ScheduledFuture<String> futureResult = scheduler.schedule(() -> {
            return "Computed result after delay";
        }, 1, TimeUnit.SECONDS);

        try {
            Thread.sleep(2500); // Wait for tasks to complete
            if (futureResult.isDone()) {
                System.out.println("   Result: " + futureResult.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        scheduler.shutdown();
        waitForTermination(scheduler);
        System.out.println();
    }

    // ========== Completion Service ==========

    private static void completionServiceDemo() {
        System.out.println("--- ExecutorCompletionService ---");
        System.out.println("Process tasks as they complete (not submission order)\n");

        ExecutorService executor = Executors.newFixedThreadPool(3);
        CompletionService<String> completionService = new ExecutorCompletionService<>(executor);

        // Submit tasks with varying durations
        System.out.println("Submitting 5 tasks with different durations:");
        int[] delays = {300, 100, 500, 200, 150};
        for (int i = 0; i < delays.length; i++) {
            final int taskNum = i + 1;
            final int delay = delays[i];
            completionService.submit(() -> {
                Thread.sleep(delay);
                return "Task " + taskNum + " (delay: " + delay + "ms)";
            });
            System.out.println("  Submitted Task " + taskNum + " (will take " + delay + "ms)");
        }

        // Retrieve results as they complete
        System.out.println("\nResults in completion order:");
        try {
            for (int i = 0; i < delays.length; i++) {
                Future<String> result = completionService.take(); // Blocks until a task completes
                System.out.println("  " + result.get() + " completed");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdown();
        waitForTermination(executor);
        System.out.println();
    }

    // ========== Executor Shutdown ==========

    private static void executorShutdownDemo() {
        System.out.println("--- Executor Shutdown Methods ---");
        System.out.println("Proper way to shut down executor services\n");

        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Submit some tasks
        for (int i = 1; i <= 3; i++) {
            final int taskNum = i;
            executor.submit(() -> {
                try {
                    Thread.sleep(500);
                    System.out.println("  Task " + taskNum + " completed");
                } catch (InterruptedException e) {
                    System.out.println("  Task " + taskNum + " interrupted");
                    Thread.currentThread().interrupt();
                }
            });
        }

        System.out.println("1. shutdown():");
        System.out.println("   ✓ Initiates orderly shutdown");
        System.out.println("   ✓ Waits for submitted tasks to complete");
        System.out.println("   ✓ Rejects new tasks");
        executor.shutdown();

        System.out.println("\n2. isShutdown(): " + executor.isShutdown());
        System.out.println("3. isTerminated(): " + executor.isTerminated() + " (tasks still running)");

        try {
            System.out.println("\n4. awaitTermination(2, TimeUnit.SECONDS):");
            System.out.println("   Waits for tasks to complete or timeout...");
            boolean terminated = executor.awaitTermination(2, TimeUnit.SECONDS);
            System.out.println("   All tasks completed: " + terminated);
            System.out.println("   isTerminated(): " + executor.isTerminated());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n5. shutdownNow():");
        System.out.println("   ✓ Attempts to stop all executing tasks");
        System.out.println("   ✓ Returns list of tasks that never started");
        System.out.println("   ✗ No guarantee tasks will stop");

        ExecutorService executor2 = Executors.newFixedThreadPool(2);
        for (int i = 1; i <= 5; i++) {
            executor2.submit(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    System.out.println("    Task interrupted by shutdownNow()");
                    Thread.currentThread().interrupt();
                }
            });
        }
        List<Runnable> notStarted = executor2.shutdownNow();
        System.out.println("   Tasks never started: " + notStarted.size());

        System.out.println();
    }

    // ========== Rejection Policies ==========

    private static void rejectionPolicyDemo() {
        System.out.println("--- Rejection Policies ---");
        System.out.println("What happens when queue is full and max threads reached\n");

        System.out.println("1. AbortPolicy (default):");
        System.out.println("   ✓ Throws RejectedExecutionException");
        System.out.println("   USE: Fail fast, caller handles rejection");

        System.out.println("\n2. CallerRunsPolicy:");
        System.out.println("   ✓ Runs task in caller's thread");
        System.out.println("   ✓ Provides backpressure");
        System.out.println("   USE: Never drop tasks, slow down submitter");

        System.out.println("\n3. DiscardPolicy:");
        System.out.println("   ✓ Silently discards task");
        System.out.println("   ✗ No feedback");
        System.out.println("   USE: Can afford to lose tasks");

        System.out.println("\n4. DiscardOldestPolicy:");
        System.out.println("   ✓ Discards oldest unhandled task");
        System.out.println("   ✓ Makes room for new task");
        System.out.println("   USE: Newer tasks more important");

        // Example: CallerRunsPolicy
        System.out.println("\nExample: CallerRunsPolicy in action");
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            1, 1, 0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(2),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );

        System.out.println("Config: core=1, max=1, queue=2");
        for (int i = 1; i <= 5; i++) {
            final int taskNum = i;
            executor.submit(() -> {
                System.out.println("  Task " + taskNum + " running on " + Thread.currentThread().getName());
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            System.out.println("  Submitted task " + taskNum);
        }

        executor.shutdown();
        waitForTermination(executor);
        System.out.println();
    }

    // ========== Custom Thread Factory ==========

    private static void threadFactoryDemo() {
        System.out.println("--- Custom Thread Factory ---");
        System.out.println("Customize thread creation with ThreadFactory\n");

        // Custom thread factory
        ThreadFactory customFactory = new ThreadFactory() {
            private AtomicInteger threadNumber = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("CustomWorker-" + threadNumber.getAndIncrement());
                t.setDaemon(false); // Non-daemon threads
                t.setPriority(Thread.NORM_PRIORITY);
                System.out.println("  Created thread: " + t.getName());
                return t;
            }
        };

        ExecutorService executor = Executors.newFixedThreadPool(3, customFactory);

        System.out.println("Submitting tasks with custom thread factory:");
        for (int i = 1; i <= 5; i++) {
            final int taskNum = i;
            executor.submit(() ->
                System.out.println("  Task " + taskNum + " on " + Thread.currentThread().getName())
            );
        }

        executor.shutdown();
        waitForTermination(executor);

        System.out.println("\nThread Factory benefits:");
        System.out.println("  ✓ Custom thread naming for debugging");
        System.out.println("  ✓ Set daemon status");
        System.out.println("  ✓ Set thread priority");
        System.out.println("  ✓ Set UncaughtExceptionHandler");
        System.out.println("  ✓ Thread pool monitoring");

        System.out.println();
    }

    // ========== Helper Methods ==========

    private static void waitForTermination(ExecutorService... executors) {
        for (ExecutorService executor : executors) {
            try {
                executor.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
