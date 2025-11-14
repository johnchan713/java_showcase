package com.example.demo.showcase;

/**
 * Demonstrates Micrometer - Application metrics facade
 * Covers metrics collection, Prometheus integration, custom metrics, and monitoring
 */
public class MicrometerShowcase {

    public static void demonstrate() {
        System.out.println("\n========== MICROMETER METRICS SHOWCASE ==========\n");

        micrometerOverviewDemo();
        counterMetricsDemo();
        gaugeMetricsDemo();
        timerMetricsDemo();
        distributionSummaryDemo();
        prometheusIntegrationDemo();
        advancedFeaturesDemo();
    }

    // ========== Overview ==========

    private static void micrometerOverviewDemo() {
        System.out.println("--- Micrometer Overview ---");
        System.out.println("Vendor-neutral metrics facade for monitoring\n");

        System.out.println("1. Key concepts:");
        System.out.println("   • MeterRegistry: Central registry for metrics");
        System.out.println("   • Counter: Monotonically increasing values");
        System.out.println("   • Gauge: Current value (can go up/down)");
        System.out.println("   • Timer: Duration and rate of events");
        System.out.println("   • DistributionSummary: Distribution of values");
        System.out.println("   • Tags: Dimensional data (key-value pairs)");

        System.out.println("\n2. Supported monitoring systems:");
        System.out.println("   • Prometheus, Graphite, Datadog, New Relic");
        System.out.println("   • StatsD, InfluxDB, Wavefront, SignalFx");
        System.out.println("   • CloudWatch, Azure Monitor, Elastic");

        System.out.println("\n3. Dependencies:");
        System.out.println("""
            <!-- Micrometer core (included with Spring Boot Actuator) -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-actuator</artifactId>
            </dependency>

            <!-- Prometheus registry -->
            <dependency>
                <groupId>io.micrometer</groupId>
                <artifactId>micrometer-registry-prometheus</artifactId>
            </dependency>

            # application.properties
            management.endpoints.web.exposure.include=health,info,metrics,prometheus
            management.metrics.export.prometheus.enabled=true
            """);

        System.out.println();
    }

    // ========== Counter Metrics ==========

    private static void counterMetricsDemo() {
        System.out.println("--- Counter Metrics ---");
        System.out.println("Track incrementing values (requests, errors, events)\n");

        System.out.println("1. Basic counter:");
        System.out.println("""
            import io.micrometer.core.instrument.Counter;
            import io.micrometer.core.instrument.MeterRegistry;

            @Service
            public class MetricsService {

                private final Counter requestCounter;
                private final Counter errorCounter;

                public MetricsService(MeterRegistry registry) {
                    this.requestCounter = Counter.builder("http.requests")
                        .description("Total HTTP requests")
                        .register(registry);

                    this.errorCounter = Counter.builder("http.errors")
                        .description("Total HTTP errors")
                        .register(registry);
                }

                public void recordRequest() {
                    requestCounter.increment();
                }

                public void recordError() {
                    errorCounter.increment();
                }

                public void recordMultiple(double amount) {
                    requestCounter.increment(amount);
                }

                public double getRequestCount() {
                    return requestCounter.count();
                }
            }
            """);

        System.out.println("2. Counter with tags:");
        System.out.println("""
            import io.micrometer.core.instrument.Tags;

            @Service
            public class TaggedMetrics {

                private final MeterRegistry registry;

                public TaggedMetrics(MeterRegistry registry) {
                    this.registry = registry;
                }

                public void recordRequest(String method, String endpoint, int status) {
                    Counter.builder("http.requests.tagged")
                        .tag("method", method)
                        .tag("endpoint", endpoint)
                        .tag("status", String.valueOf(status))
                        .register(registry)
                        .increment();
                }

                public void recordUserEvent(String userId, String eventType) {
                    Counter.builder("user.events")
                        .tags(Tags.of(
                            "user_id", userId,
                            "event_type", eventType
                        ))
                        .register(registry)
                        .increment();
                }
            }

            // Query metrics by tags
            // Example Prometheus query: http_requests_tagged{method="GET",status="200"}
            """);

        System.out.println("3. Function counter:");
        System.out.println("""
            import io.micrometer.core.instrument.FunctionCounter;

            @Component
            public class CacheMetrics {

                private final AtomicLong cacheHits = new AtomicLong(0);
                private final AtomicLong cacheMisses = new AtomicLong(0);

                public CacheMetrics(MeterRegistry registry) {
                    // Automatically tracks the value
                    FunctionCounter.builder("cache.hits", cacheHits, AtomicLong::get)
                        .description("Cache hit count")
                        .register(registry);

                    FunctionCounter.builder("cache.misses", cacheMisses, AtomicLong::get)
                        .description("Cache miss count")
                        .register(registry);
                }

                public void recordHit() {
                    cacheHits.incrementAndGet();
                }

                public void recordMiss() {
                    cacheMisses.incrementAndGet();
                }
            }
            """);

        System.out.println();
    }

    // ========== Gauge Metrics ==========

    private static void gaugeMetricsDemo() {
        System.out.println("--- Gauge Metrics ---");
        System.out.println("Track current values (queue size, active users, memory)\n");

        System.out.println("1. Basic gauge:");
        System.out.println("""
            import io.micrometer.core.instrument.Gauge;

            @Service
            public class QueueMetrics {

                private final Queue<String> messageQueue = new ConcurrentLinkedQueue<>();

                public QueueMetrics(MeterRegistry registry) {
                    Gauge.builder("queue.size", messageQueue, Queue::size)
                        .description("Current queue size")
                        .register(registry);
                }

                public void addMessage(String message) {
                    messageQueue.offer(message);
                }

                public String pollMessage() {
                    return messageQueue.poll();
                }
            }
            """);

        System.out.println("2. Gauge with atomic reference:");
        System.out.println("""
            @Component
            public class SystemMetrics {

                private final AtomicInteger activeUsers = new AtomicInteger(0);
                private final AtomicDouble cpuUsage = new AtomicDouble(0.0);

                public SystemMetrics(MeterRegistry registry) {
                    Gauge.builder("system.active_users", activeUsers, AtomicInteger::get)
                        .description("Currently active users")
                        .register(registry);

                    Gauge.builder("system.cpu_usage", cpuUsage, AtomicDouble::get)
                        .description("CPU usage percentage")
                        .tag("host", "localhost")
                        .register(registry);
                }

                public void userLogin() {
                    activeUsers.incrementAndGet();
                }

                public void userLogout() {
                    activeUsers.decrementAndGet();
                }

                public void updateCpuUsage(double usage) {
                    cpuUsage.set(usage);
                }
            }
            """);

        System.out.println("3. Multi-gauge (multiple related gauges):");
        System.out.println("""
            import io.micrometer.core.instrument.MultiGauge;

            @Component
            public class ConnectionPoolMetrics {

                private final MultiGauge poolGauges;

                public ConnectionPoolMetrics(MeterRegistry registry) {
                    poolGauges = MultiGauge.builder("db.connection.pool")
                        .description("Database connection pool metrics")
                        .register(registry);
                }

                public void updatePoolMetrics(Map<String, Integer> pools) {
                    List<MultiGauge.Row<?>> rows = pools.entrySet().stream()
                        .map(entry -> MultiGauge.Row.of(
                            Tags.of("pool", entry.getKey()),
                            entry.getValue()
                        ))
                        .collect(Collectors.toList());

                    poolGauges.register(rows);
                }
            }
            """);

        System.out.println();
    }

    // ========== Timer Metrics ==========

    private static void timerMetricsDemo() {
        System.out.println("--- Timer Metrics ---");
        System.out.println("Measure duration and throughput of operations\n");

        System.out.println("1. Basic timer:");
        System.out.println("""
            import io.micrometer.core.instrument.Timer;

            @Service
            public class PerformanceMetrics {

                private final Timer requestTimer;

                public PerformanceMetrics(MeterRegistry registry) {
                    this.requestTimer = Timer.builder("http.request.duration")
                        .description("HTTP request duration")
                        .tag("service", "user-service")
                        .register(registry);
                }

                public void processRequest() {
                    requestTimer.record(() -> {
                        // Execute operation
                        performBusinessLogic();
                    });
                }

                public <T> T executeWithTiming(Supplier<T> operation) {
                    return requestTimer.record(operation);
                }
            }
            """);

        System.out.println("2. Manual timing:");
        System.out.println("""
            import java.time.Duration;

            public void manualTiming() {
                Timer.Sample sample = Timer.start(registry);

                try {
                    // Perform operation
                    processData();
                } finally {
                    sample.stop(Timer.builder("data.processing")
                        .register(registry));
                }
            }

            public void recordDuration(Duration duration) {
                Timer.builder("operation.time")
                    .register(registry)
                    .record(duration);
            }
            """);

        System.out.println("3. Timer with percentiles:");
        System.out.println("""
            Timer responseTimer = Timer.builder("api.response.time")
                .description("API response time")
                .publishPercentiles(0.5, 0.95, 0.99)  // p50, p95, p99
                .publishPercentileHistogram()
                .minimumExpectedValue(Duration.ofMillis(1))
                .maximumExpectedValue(Duration.ofSeconds(10))
                .register(registry);

            // Metrics available:
            // api_response_time_seconds{quantile="0.5"}   - Median
            // api_response_time_seconds{quantile="0.95"}  - 95th percentile
            // api_response_time_seconds{quantile="0.99"}  - 99th percentile
            // api_response_time_seconds_count             - Total count
            // api_response_time_seconds_sum               - Total duration
            """);

        System.out.println("4. @Timed annotation:");
        System.out.println("""
            import io.micrometer.core.annotation.Timed;

            @Service
            public class UserService {

                @Timed(value = "user.find", description = "Time to find user")
                public User findUser(Long id) {
                    return userRepository.findById(id);
                }

                @Timed(
                    value = "user.create",
                    description = "Time to create user",
                    percentiles = {0.5, 0.95, 0.99},
                    histogram = true
                )
                public User createUser(User user) {
                    return userRepository.save(user);
                }
            }

            // Enable @Timed
            @Configuration
            public class MetricsConfig {
                @Bean
                public TimedAspect timedAspect(MeterRegistry registry) {
                    return new TimedAspect(registry);
                }
            }
            """);

        System.out.println();
    }

    // ========== Distribution Summary ==========

    private static void distributionSummaryDemo() {
        System.out.println("--- Distribution Summary ---");
        System.out.println("Track distribution of values (payload size, response size)\n");

        System.out.println("1. Basic distribution summary:");
        System.out.println("""
            import io.micrometer.core.instrument.DistributionSummary;

            @Service
            public class PayloadMetrics {

                private final DistributionSummary requestSize;
                private final DistributionSummary responseSize;

                public PayloadMetrics(MeterRegistry registry) {
                    this.requestSize = DistributionSummary.builder("http.request.size")
                        .description("HTTP request payload size")
                        .baseUnit("bytes")
                        .register(registry);

                    this.responseSize = DistributionSummary.builder("http.response.size")
                        .description("HTTP response payload size")
                        .baseUnit("bytes")
                        .publishPercentiles(0.5, 0.95, 0.99)
                        .register(registry);
                }

                public void recordRequest(int size) {
                    requestSize.record(size);
                }

                public void recordResponse(int size) {
                    responseSize.record(size);
                }
            }
            """);

        System.out.println("2. Distribution with histogram:");
        System.out.println("""
            DistributionSummary orderAmount = DistributionSummary.builder("order.amount")
                .description("Order amounts")
                .baseUnit("dollars")
                .publishPercentileHistogram()
                .minimumExpectedValue(1.0)
                .maximumExpectedValue(10000.0)
                .register(registry);

            orderAmount.record(49.99);
            orderAmount.record(199.99);

            // Get statistics
            long count = orderAmount.count();
            double total = orderAmount.totalAmount();
            double max = orderAmount.max();
            double mean = orderAmount.mean();
            """);

        System.out.println();
    }

    // ========== Prometheus Integration ==========

    private static void prometheusIntegrationDemo() {
        System.out.println("--- Prometheus Integration ---");
        System.out.println("Export metrics to Prometheus\n");

        System.out.println("1. Configuration:");
        System.out.println("""
            # application.properties

            # Enable Prometheus endpoint
            management.endpoints.web.exposure.include=prometheus,health,info
            management.endpoint.prometheus.enabled=true
            management.metrics.export.prometheus.enabled=true

            # Customize metrics
            management.metrics.tags.application=my-app
            management.metrics.tags.environment=production

            # Metric filters
            management.metrics.enable.jvm=true
            management.metrics.enable.process=true
            management.metrics.enable.system=true
            """);

        System.out.println("2. Access Prometheus metrics:");
        System.out.println("""
            # Metrics endpoint
            GET http://localhost:8080/actuator/prometheus

            # Example output:
            # HELP http_requests_total Total HTTP requests
            # TYPE http_requests_total counter
            http_requests_total{method="GET",status="200"} 1543.0
            http_requests_total{method="POST",status="201"} 89.0

            # HELP http_request_duration_seconds HTTP request duration
            # TYPE http_request_duration_seconds summary
            http_request_duration_seconds{quantile="0.5"} 0.023
            http_request_duration_seconds{quantile="0.95"} 0.145
            http_request_duration_seconds{quantile="0.99"} 0.312
            http_request_duration_seconds_count 1632.0
            http_request_duration_seconds_sum 45.678
            """);

        System.out.println("3. Prometheus scrape configuration:");
        System.out.println("""
            # prometheus.yml
            scrape_configs:
              - job_name: 'spring-boot-app'
                metrics_path: '/actuator/prometheus'
                scrape_interval: 15s
                static_configs:
                  - targets: ['localhost:8080']
                    labels:
                      application: 'my-app'
                      environment: 'production'
            """);

        System.out.println("4. Common PromQL queries:");
        System.out.println("""
            # Request rate (requests per second)
            rate(http_requests_total[5m])

            # Request rate by status code
            sum by (status) (rate(http_requests_total[5m]))

            # Average response time
            rate(http_request_duration_seconds_sum[5m]) /
            rate(http_request_duration_seconds_count[5m])

            # 95th percentile response time
            http_request_duration_seconds{quantile="0.95"}

            # Error rate percentage
            sum(rate(http_requests_total{status=~"5.."}[5m])) /
            sum(rate(http_requests_total[5m])) * 100
            """);

        System.out.println();
    }

    // ========== Advanced Features ==========

    private static void advancedFeaturesDemo() {
        System.out.println("--- Advanced Features ---");
        System.out.println("Custom meters, common tags, and best practices\n");

        System.out.println("1. Common tags (applied to all metrics):");
        System.out.println("""
            @Configuration
            public class MetricsConfig {

                @Bean
                public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
                    return registry -> registry.config()
                        .commonTags(
                            "application", "my-app",
                            "environment", getEnvironment(),
                            "region", "us-east-1",
                            "version", getAppVersion()
                        );
                }
            }
            """);

        System.out.println("2. Metric filters:");
        System.out.println("""
            @Bean
            public MeterFilter meterFilter() {
                return new MeterFilter() {
                    @Override
                    public MeterFilterReply accept(Meter.Id id) {
                        // Deny specific metrics
                        if (id.getName().startsWith("jvm.")) {
                            return MeterFilterReply.DENY;
                        }
                        return MeterFilterReply.NEUTRAL;
                    }

                    @Override
                    public Meter.Id map(Meter.Id id) {
                        // Rename metrics
                        if (id.getName().startsWith("http.")) {
                            return id.withName("api." + id.getName().substring(5));
                        }
                        return id;
                    }
                };
            }

            // Deny all JVM metrics
            MeterFilter.denyNameStartsWith("jvm")

            // Only accept specific metrics
            MeterFilter.acceptNameStartsWith("http")

            // Limit maximum tags
            MeterFilter.maximumAllowableTags("http.requests", 100, MeterFilter.deny())
            """);

        System.out.println("3. Custom meter binder:");
        System.out.println("""
            import io.micrometer.core.instrument.binder.MeterBinder;

            @Component
            public class CustomMetricsBinder implements MeterBinder {

                private final ApplicationService applicationService;

                public CustomMetricsBinder(ApplicationService applicationService) {
                    this.applicationService = applicationService;
                }

                @Override
                public void bindTo(MeterRegistry registry) {
                    // Gauge for active connections
                    Gauge.builder("app.active_connections",
                            applicationService, ApplicationService::getActiveConnections)
                        .description("Active database connections")
                        .register(registry);

                    // Gauge for queue depth
                    Gauge.builder("app.queue_depth",
                            applicationService, ApplicationService::getQueueDepth)
                        .description("Message queue depth")
                        .register(registry);
                }
            }
            """);

        System.out.println("4. Built-in meters:");
        System.out.println("""
            # JVM Metrics (automatic)
            - jvm.memory.used
            - jvm.memory.max
            - jvm.gc.pause
            - jvm.threads.live
            - jvm.threads.daemon
            - jvm.classes.loaded

            # Process Metrics
            - process.cpu.usage
            - process.uptime
            - process.files.open

            # System Metrics
            - system.cpu.count
            - system.cpu.usage
            - system.load.average.1m

            # HTTP Metrics (Spring Boot)
            - http.server.requests
            - http.server.requests.max

            # Tomcat Metrics
            - tomcat.sessions.active.current
            - tomcat.sessions.active.max
            - tomcat.threads.busy
            - tomcat.threads.current
            """);

        System.out.println("5. Best practices:");
        System.out.println("   ✓ Use meaningful metric names (lowercase, dots/underscores)");
        System.out.println("   ✓ Add descriptive tags for dimensionality");
        System.out.println("   ✓ Use counters for monotonically increasing values");
        System.out.println("   ✓ Use gauges for values that go up and down");
        System.out.println("   ✓ Use timers for measuring duration and rate");
        System.out.println("   ✓ Enable percentile histograms for latency metrics");
        System.out.println("   ✓ Apply common tags for filtering");
        System.out.println("   ✓ Monitor cardinality (avoid high-cardinality tags)");
        System.out.println("   ✗ Don't use timestamps or UUIDs as tag values");
        System.out.println("   ✗ Don't create unbounded number of metrics");
        System.out.println("   ✗ Don't use gauge for events (use counter)");

        System.out.println("\n6. Grafana dashboard example:");
        System.out.println("""
            # Request rate panel
            sum(rate(http_server_requests_seconds_count[5m])) by (uri)

            # Error rate panel
            sum(rate(http_server_requests_seconds_count{status=~"5.."}[5m])) /
            sum(rate(http_server_requests_seconds_count[5m])) * 100

            # Response time panel
            histogram_quantile(0.95,
              sum(rate(http_server_requests_seconds_bucket[5m])) by (le, uri)
            )

            # JVM memory panel
            jvm_memory_used_bytes{area="heap"}
            """);

        System.out.println();
    }
}
