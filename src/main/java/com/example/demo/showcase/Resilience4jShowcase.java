package com.example.demo.showcase;

/**
 * Demonstrates Resilience4j for fault tolerance
 * Covers Circuit Breaker, Retry, Rate Limiter, Bulkhead, and TimeLimiter
 */
public class Resilience4jShowcase {

    public static void demonstrate() {
        System.out.println("\n========== RESILIENCE4J SHOWCASE ==========\n");

        resilience4jOverviewDemo();
        circuitBreakerDemo();
        retryDemo();
        rateLimiterDemo();
        bulkheadDemo();
        timeLimiterDemo();
        combinedDemo();
    }

    // ========== Overview ==========

    private static void resilience4jOverviewDemo() {
        System.out.println("--- Resilience4j Overview ---");
        System.out.println("Fault tolerance library for Java\n");

        System.out.println("1. Resilience patterns:");
        System.out.println("   Circuit Breaker: Prevent cascading failures");
        System.out.println("   Retry: Automatic retry on transient failures");
        System.out.println("   Rate Limiter: Limit number of calls in time period");
        System.out.println("   Bulkhead: Limit concurrent executions");
        System.out.println("   Time Limiter: Timeout for async operations");
        System.out.println("   Cache: Memoization of results");

        System.out.println("\n2. Dependencies:");
        System.out.println("""
            <dependency>
                <groupId>io.github.resilience4j</groupId>
                <artifactId>resilience4j-spring-boot3</artifactId>
                <version>2.1.0</version>
            </dependency>

            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-aop</artifactId>
            </dependency>
            """);

        System.out.println();
    }

    // ========== Circuit Breaker ==========

    private static void circuitBreakerDemo() {
        System.out.println("--- Circuit Breaker ---");
        System.out.println("Prevent repeated calls to failing services\n");

        System.out.println("1. Configuration:");
        System.out.println("""
            # application.yml
            resilience4j.circuitbreaker:
              instances:
                backendA:
                  registerHealthIndicator: true
                  slidingWindowSize: 10                    # Number of calls to record
                  minimumNumberOfCalls: 5                  # Min calls before calculation
                  permittedNumberOfCallsInHalfOpenState: 3 # Calls in half-open state
                  automaticTransitionFromOpenToHalfOpenEnabled: true
                  waitDurationInOpenState: 5s              # Wait before half-open
                  failureRateThreshold: 50                 # % failures to open
                  slowCallRateThreshold: 100               # % slow calls to open
                  slowCallDurationThreshold: 2s            # Duration for slow call
                  recordExceptions:
                    - java.io.IOException
                    - java.util.concurrent.TimeoutException
                  ignoreExceptions:
                    - com.example.BusinessException

            # Java Configuration
            @Configuration
            public class CircuitBreakerConfig {

                @Bean
                public CircuitBreakerRegistry circuitBreakerRegistry() {
                    CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                        .slidingWindowSize(10)
                        .minimumNumberOfCalls(5)
                        .failureRateThreshold(50)
                        .waitDurationInOpenState(Duration.ofSeconds(5))
                        .permittedNumberOfCallsInHalfOpenState(3)
                        .automaticTransitionFromOpenToHalfOpenEnabled(true)
                        .recordExceptions(IOException.class, TimeoutException.class)
                        .ignoreExceptions(BusinessException.class)
                        .build();

                    return CircuitBreakerRegistry.of(config);
                }
            }
            """);

        System.out.println("2. Usage with annotations:");
        System.out.println("""
            @Service
            public class BackendService {

                @CircuitBreaker(name = "backendA", fallbackMethod = "fallback")
                public String callExternalService() {
                    // Call to external service that might fail
                    return restTemplate.getForObject("http://backend/api", String.class);
                }

                // Fallback method (same signature + exception parameter)
                public String fallback(Exception e) {
                    return "Fallback response due to: " + e.getMessage();
                }

                // Fallback with specific exception
                public String fallback(IOException e) {
                    return "Network error: " + e.getMessage();
                }
            }
            """);

        System.out.println("3. Programmatic usage:");
        System.out.println("""
            @Service
            public class BackendService {

                @Autowired
                private CircuitBreakerRegistry circuitBreakerRegistry;

                public String callWithCircuitBreaker() {
                    CircuitBreaker circuitBreaker =
                        circuitBreakerRegistry.circuitBreaker("backendA");

                    // Decorate supplier
                    Supplier<String> decoratedSupplier =
                        CircuitBreaker.decorateSupplier(circuitBreaker, () -> {
                            return callExternalService();
                        });

                    // Execute with fallback
                    try {
                        return decoratedSupplier.get();
                    } catch (Exception e) {
                        return "Fallback response";
                    }
                }

                // With Try
                public String callWithTry() {
                    CircuitBreaker circuitBreaker =
                        circuitBreakerRegistry.circuitBreaker("backendA");

                    Try<String> result = Try.ofSupplier(
                        CircuitBreaker.decorateSupplier(circuitBreaker,
                            this::callExternalService)
                    );

                    return result.getOrElse("Fallback");
                }
            }
            """);

        System.out.println("4. Circuit Breaker states:");
        System.out.println("   CLOSED (Normal):");
        System.out.println("     - Requests pass through");
        System.out.println("     - Failures are counted");
        System.out.println("     - Opens if failure rate > threshold");
        System.out.println("\n   OPEN (Failing):");
        System.out.println("     - All requests immediately fail");
        System.out.println("     - Returns error without calling service");
        System.out.println("     - After wait duration, transitions to HALF_OPEN");
        System.out.println("\n   HALF_OPEN (Testing):");
        System.out.println("     - Limited number of test requests");
        System.out.println("     - If successful: back to CLOSED");
        System.out.println("     - If failed: back to OPEN");

        System.out.println("\n5. Event monitoring:");
        System.out.println("""
            @Component
            public class CircuitBreakerEventListener {

                @Autowired
                private CircuitBreakerRegistry circuitBreakerRegistry;

                @PostConstruct
                public void init() {
                    circuitBreakerRegistry.circuitBreaker("backendA")
                        .getEventPublisher()
                        .onSuccess(event ->
                            log.info("Call succeeded"))
                        .onError(event ->
                            log.error("Call failed: {}", event.getThrowable()))
                        .onStateTransition(event ->
                            log.warn("Circuit breaker state changed: {} -> {}",
                                event.getStateTransition().getFromState(),
                                event.getStateTransition().getToState()))
                        .onSlowCallRateExceeded(event ->
                            log.warn("Slow call rate exceeded"))
                        .onFailureRateExceeded(event ->
                            log.error("Failure rate exceeded: {}%",
                                event.getFailureRate()));
                }
            }
            """);

        System.out.println();
    }

    // ========== Retry ==========

    private static void retryDemo() {
        System.out.println("--- Retry ---");
        System.out.println("Automatic retry on failures\n");

        System.out.println("1. Configuration:");
        System.out.println("""
            # application.yml
            resilience4j.retry:
              instances:
                backendA:
                  maxAttempts: 3                    # Max retry attempts
                  waitDuration: 1s                  # Wait between retries
                  enableExponentialBackoff: true    # Exponential backoff
                  exponentialBackoffMultiplier: 2   # Backoff multiplier
                  retryExceptions:
                    - java.io.IOException
                  ignoreExceptions:
                    - com.example.BusinessException

            # Java Configuration
            @Configuration
            public class RetryConfig {

                @Bean
                public RetryRegistry retryRegistry() {
                    io.github.resilience4j.retry.RetryConfig config =
                        io.github.resilience4j.retry.RetryConfig.custom()
                            .maxAttempts(3)
                            .waitDuration(Duration.ofSeconds(1))
                            .retryOnException(e -> e instanceof IOException)
                            .ignoreExceptions(BusinessException.class)
                            .build();

                    return RetryRegistry.of(config);
                }

                // Exponential backoff
                @Bean
                public RetryRegistry exponentialRetryRegistry() {
                    io.github.resilience4j.retry.RetryConfig config =
                        io.github.resilience4j.retry.RetryConfig.custom()
                            .maxAttempts(5)
                            .intervalFunction(IntervalFunction
                                .ofExponentialBackoff(1000, 2))  // 1s, 2s, 4s, 8s, 16s
                            .build();

                    return RetryRegistry.of(config);
                }
            }
            """);

        System.out.println("2. Usage with annotations:");
        System.out.println("""
            @Service
            public class BackendService {

                @Retry(name = "backendA", fallbackMethod = "fallback")
                public String callWithRetry() {
                    System.out.println("Attempting call...");
                    return restTemplate.getForObject("http://backend/api", String.class);
                }

                public String fallback(Exception e) {
                    return "Failed after retries: " + e.getMessage();
                }
            }
            """);

        System.out.println("3. Programmatic usage:");
        System.out.println("""
            @Service
            public class BackendService {

                @Autowired
                private RetryRegistry retryRegistry;

                public String callWithRetry() {
                    Retry retry = retryRegistry.retry("backendA");

                    Supplier<String> decoratedSupplier =
                        Retry.decorateSupplier(retry, this::callExternalService);

                    return Try.ofSupplier(decoratedSupplier)
                        .getOrElse("Fallback after retries");
                }
            }
            """);

        System.out.println("4. Event monitoring:");
        System.out.println("""
            retry.getEventPublisher()
                .onRetry(event ->
                    log.info("Retry attempt: {} of {}",
                        event.getNumberOfRetryAttempts(),
                        retry.getRetryConfig().getMaxAttempts()))
                .onSuccess(event ->
                    log.info("Call succeeded after {} retries",
                        event.getNumberOfRetryAttempts()))
                .onError(event ->
                    log.error("All retries failed: {}",
                        event.getLastThrowable()));
            """);

        System.out.println();
    }

    // ========== Rate Limiter ==========

    private static void rateLimiterDemo() {
        System.out.println("--- Rate Limiter ---");
        System.out.println("Limit number of calls in time period\n");

        System.out.println("1. Configuration:");
        System.out.println("""
            # application.yml
            resilience4j.ratelimiter:
              instances:
                backendA:
                  limitForPeriod: 10          # Max calls per period
                  limitRefreshPeriod: 1s      # Period duration
                  timeoutDuration: 0          # Wait time for permission (0 = fail immediately)

            # Java Configuration
            @Configuration
            public class RateLimiterConfig {

                @Bean
                public RateLimiterRegistry rateLimiterRegistry() {
                    io.github.resilience4j.ratelimiter.RateLimiterConfig config =
                        io.github.resilience4j.ratelimiter.RateLimiterConfig.custom()
                            .limitForPeriod(10)                    // 10 calls
                            .limitRefreshPeriod(Duration.ofSeconds(1))  // per second
                            .timeoutDuration(Duration.ofMillis(100))    // Wait 100ms
                            .build();

                    return RateLimiterRegistry.of(config);
                }
            }
            """);

        System.out.println("2. Usage with annotations:");
        System.out.println("""
            @Service
            public class ApiService {

                @RateLimiter(name = "backendA", fallbackMethod = "fallback")
                public String callApi() {
                    return "API response";
                }

                public String fallback(RequestNotPermitted e) {
                    return "Too many requests. Please try again later.";
                }
            }
            """);

        System.out.println("3. Programmatic usage:");
        System.out.println("""
            @Service
            public class ApiService {

                @Autowired
                private RateLimiterRegistry rateLimiterRegistry;

                public String callWithRateLimit() {
                    RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter("backendA");

                    Supplier<String> decoratedSupplier =
                        RateLimiter.decorateSupplier(rateLimiter, this::callApi);

                    try {
                        return decoratedSupplier.get();
                    } catch (RequestNotPermitted e) {
                        return "Rate limit exceeded";
                    }
                }

                // Check permission first
                public String callWithCheck() {
                    RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter("backendA");

                    if (rateLimiter.acquirePermission()) {
                        try {
                            return callApi();
                        } finally {
                            // Permission released automatically
                        }
                    } else {
                        return "Rate limit exceeded";
                    }
                }
            }
            """);

        System.out.println("4. Metrics:");
        System.out.println("""
            RateLimiter.Metrics metrics = rateLimiter.getMetrics();
            int availablePermissions = metrics.getAvailablePermissions();
            int numberOfWaitingThreads = metrics.getNumberOfWaitingThreads();
            """);

        System.out.println();
    }

    // ========== Bulkhead ==========

    private static void bulkheadDemo() {
        System.out.println("--- Bulkhead ---");
        System.out.println("Limit concurrent executions\n");

        System.out.println("1. Configuration:");
        System.out.println("""
            # application.yml
            resilience4j.bulkhead:
              instances:
                backendA:
                  maxConcurrentCalls: 10      # Max concurrent calls
                  maxWaitDuration: 100ms      # Max wait for permission

            # Thread pool bulkhead (for async operations)
            resilience4j.thread-pool-bulkhead:
              instances:
                backendA:
                  maxThreadPoolSize: 4        # Max threads
                  coreThreadPoolSize: 2       # Core threads
                  queueCapacity: 2            # Queue size
                  keepAliveDuration: 20ms

            # Java Configuration
            @Configuration
            public class BulkheadConfig {

                @Bean
                public BulkheadRegistry bulkheadRegistry() {
                    io.github.resilience4j.bulkhead.BulkheadConfig config =
                        io.github.resilience4j.bulkhead.BulkheadConfig.custom()
                            .maxConcurrentCalls(10)
                            .maxWaitDuration(Duration.ofMillis(100))
                            .build();

                    return BulkheadRegistry.of(config);
                }

                @Bean
                public ThreadPoolBulkheadRegistry threadPoolBulkheadRegistry() {
                    io.github.resilience4j.bulkhead.ThreadPoolBulkheadConfig config =
                        io.github.resilience4j.bulkhead.ThreadPoolBulkheadConfig.custom()
                            .maxThreadPoolSize(4)
                            .coreThreadPoolSize(2)
                            .queueCapacity(2)
                            .build();

                    return ThreadPoolBulkheadRegistry.of(config);
                }
            }
            """);

        System.out.println("2. Usage with annotations:");
        System.out.println("""
            @Service
            public class BackendService {

                // Semaphore-based bulkhead (synchronous)
                @Bulkhead(name = "backendA", fallbackMethod = "fallback")
                public String callWithBulkhead() {
                    return heavyOperation();
                }

                // Thread pool bulkhead (asynchronous)
                @Bulkhead(name = "backendA", type = Bulkhead.Type.THREADPOOL)
                public CompletableFuture<String> callAsync() {
                    return CompletableFuture.supplyAsync(this::heavyOperation);
                }

                public String fallback(BulkheadFullException e) {
                    return "Too many concurrent calls";
                }

                public CompletableFuture<String> fallbackAsync(BulkheadFullException e) {
                    return CompletableFuture.completedFuture("Too many concurrent calls");
                }
            }
            """);

        System.out.println("3. Use cases:");
        System.out.println("   ✓ Protect resource pools (database connections, threads)");
        System.out.println("   ✓ Prevent thread starvation");
        System.out.println("   ✓ Isolate failures in microservices");
        System.out.println("   ✓ Control concurrent access to external APIs");

        System.out.println();
    }

    // ========== Time Limiter ==========

    private static void timeLimiterDemo() {
        System.out.println("--- Time Limiter ---");
        System.out.println("Timeout for async operations\n");

        System.out.println("1. Configuration:");
        System.out.println("""
            # application.yml
            resilience4j.timelimiter:
              instances:
                backendA:
                  timeoutDuration: 2s         # Max duration
                  cancelRunningFuture: true   # Cancel if timeout

            # Java Configuration
            @Configuration
            public class TimeLimiterConfig {

                @Bean
                public TimeLimiterRegistry timeLimiterRegistry() {
                    io.github.resilience4j.timelimiter.TimeLimiterConfig config =
                        io.github.resilience4j.timelimiter.TimeLimiterConfig.custom()
                            .timeoutDuration(Duration.ofSeconds(2))
                            .cancelRunningFuture(true)
                            .build();

                    return TimeLimiterRegistry.of(config);
                }
            }
            """);

        System.out.println("2. Usage with annotations:");
        System.out.println("""
            @Service
            public class BackendService {

                @TimeLimiter(name = "backendA", fallbackMethod = "fallback")
                public CompletableFuture<String> callAsync() {
                    return CompletableFuture.supplyAsync(() -> {
                        // Long-running operation
                        return slowOperation();
                    });
                }

                public CompletableFuture<String> fallback(TimeoutException e) {
                    return CompletableFuture.completedFuture("Operation timed out");
                }
            }
            """);

        System.out.println("3. Programmatic usage:");
        System.out.println("""
            @Service
            public class BackendService {

                @Autowired
                private TimeLimiterRegistry timeLimiterRegistry;

                @Autowired
                private ExecutorService executorService;

                public String callWithTimeout() {
                    TimeLimiter timeLimiter = timeLimiterRegistry.timeLimiter("backendA");

                    Supplier<CompletableFuture<String>> futureSupplier =
                        () -> CompletableFuture.supplyAsync(this::slowOperation);

                    Callable<String> callable =
                        TimeLimiter.decorateFutureSupplier(timeLimiter, futureSupplier);

                    try {
                        return callable.call();
                    } catch (TimeoutException e) {
                        return "Timeout";
                    } catch (Exception e) {
                        return "Error";
                    }
                }
            }
            """);

        System.out.println();
    }

    // ========== Combined Patterns ==========

    private static void combinedDemo() {
        System.out.println("--- Combining Resilience Patterns ---");
        System.out.println("Use multiple patterns together\n");

        System.out.println("1. Circuit Breaker + Retry:");
        System.out.println("""
            @Service
            public class ResilientService {

                @CircuitBreaker(name = "backendA")
                @Retry(name = "backendA", fallbackMethod = "fallback")
                public String resilientCall() {
                    // Retry is executed first, then circuit breaker
                    // Order: @Retry → @CircuitBreaker → method
                    return callExternalService();
                }

                public String fallback(Exception e) {
                    return "Fallback response";
                }
            }
            """);

        System.out.println("2. Rate Limiter + Circuit Breaker + Retry:");
        System.out.println("""
            @RateLimiter(name = "backendA")
            @CircuitBreaker(name = "backendA")
            @Retry(name = "backendA", fallbackMethod = "fallback")
            public String fullyResilient() {
                // Order: @Retry → @CircuitBreaker → @RateLimiter → method
                return callExternalService();
            }
            """);

        System.out.println("3. Bulkhead + Time Limiter + Circuit Breaker:");
        System.out.println("""
            @Bulkhead(name = "backendA", type = Bulkhead.Type.THREADPOOL)
            @TimeLimiter(name = "backendA")
            @CircuitBreaker(name = "backendA", fallbackMethod = "fallbackAsync")
            public CompletableFuture<String> asyncResilient() {
                return CompletableFuture.supplyAsync(this::callExternalService);
            }

            public CompletableFuture<String> fallbackAsync(Exception e) {
                return CompletableFuture.completedFuture("Async fallback");
            }
            """);

        System.out.println("4. Best practices:");
        System.out.println("   ✓ Use circuit breaker for external service calls");
        System.out.println("   ✓ Add retry for transient failures (network glitches)");
        System.out.println("   ✓ Use rate limiter to protect your service");
        System.out.println("   ✓ Use bulkhead to isolate resource pools");
        System.out.println("   ✓ Add time limiter for async operations");
        System.out.println("   ✓ Monitor metrics and events");
        System.out.println("   ✓ Test failover scenarios");
        System.out.println("   ✗ Don't retry non-idempotent operations without care");
        System.out.println("   ✗ Don't ignore exceptions in fallback methods");

        System.out.println("\n5. Monitoring with Actuator:");
        System.out.println("""
            # application.yml
            management.endpoints.web.exposure.include: health,metrics,circuitbreakers
            management.health.circuitbreakers.enabled: true

            # Metrics endpoints
            GET /actuator/health
            GET /actuator/circuitbreakers
            GET /actuator/metrics/resilience4j.circuitbreaker.calls
            GET /actuator/metrics/resilience4j.retry.calls
            GET /actuator/metrics/resilience4j.ratelimiter.available.permissions
            """);

        System.out.println();
    }
}
