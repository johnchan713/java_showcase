package com.example.demo.showcase;

/**
 * Demonstrates SLF4J and Logback logging (Spring Boot default)
 * Covers log levels, configuration, MDC, structured logging, and best practices
 *
 * Note: Spring Boot uses SLF4J with Logback by default (not Log4j)
 * Log4j 2 can be used as an alternative with additional configuration
 */
public class LoggingShowcase {

    public static void demonstrate() {
        System.out.println("\n========== LOGGING SHOWCASE (SLF4J + Logback) ==========\n");

        loggingOverviewDemo();
        logLevelsDemo();
        logbackConfigurationDemo();
        mdcDemo();
        structuredLoggingDemo();
        advancedFeaturesDemo();
        log4j2AlternativeDemo();
    }

    // ========== Logging Overview ==========

    private static void loggingOverviewDemo() {
        System.out.println("--- Logging Overview ---");
        System.out.println("SLF4J facade with Logback implementation\n");

        System.out.println("1. Logging architecture:");
        System.out.println("   SLF4J (Simple Logging Facade for Java):");
        System.out.println("     - Logging abstraction/API");
        System.out.println("     - Allows switching implementations");
        System.out.println("   Logback:");
        System.out.println("     - Implementation of SLF4J");
        System.out.println("     - Spring Boot default");
        System.out.println("     - High performance, feature-rich");

        System.out.println("\n2. Why SLF4J + Logback:");
        System.out.println("   ✓ Standard in Spring Boot (no additional config)");
        System.out.println("   ✓ Better performance than Log4j 1.x");
        System.out.println("   ✓ Native SLF4J implementation");
        System.out.println("   ✓ Automatic configuration reloading");
        System.out.println("   ✓ Advanced filtering and routing");

        System.out.println("\n3. Dependencies (Spring Boot includes by default):");
        System.out.println("""
            <!-- Included in spring-boot-starter -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-logging</artifactId>
            </dependency>

            <!-- Which transitively includes: -->
            <!-- - slf4j-api -->
            <!-- - logback-classic -->
            <!-- - logback-core -->
            <!-- - log4j-to-slf4j (bridge) -->
            <!-- - jul-to-slf4j (bridge) -->
            """);

        System.out.println();
    }

    // ========== Log Levels ==========

    private static void logLevelsDemo() {
        System.out.println("--- Log Levels ---");
        System.out.println("Different severity levels for logging\n");

        System.out.println("1. Basic usage:");
        System.out.println("""
            import org.slf4j.Logger;
            import org.slf4j.LoggerFactory;

            @Service
            public class MyService {

                private static final Logger log = LoggerFactory.getLogger(MyService.class);

                // Alternative: Lombok
                @Slf4j
                public class MyService {
                    // 'log' field automatically generated
                }

                public void demonstrateLevels() {
                    // TRACE - Very detailed, diagnostic
                    log.trace("Entering method with parameter: {}", param);

                    // DEBUG - Diagnostic information
                    log.debug("Processing user: {}, status: {}", userId, status);

                    // INFO - Informational messages
                    log.info("User {} registered successfully", username);

                    // WARN - Warning messages (potential issues)
                    log.warn("Deprecated method called: {}", methodName);

                    // ERROR - Error events
                    log.error("Failed to process order {}: {}", orderId, e.getMessage(), e);
                }
            }
            """);

        System.out.println("2. Log levels hierarchy:");
        System.out.println("   TRACE < DEBUG < INFO < WARN < ERROR");
        System.out.println("   If level set to INFO:");
        System.out.println("     - TRACE: Not logged");
        System.out.println("     - DEBUG: Not logged");
        System.out.println("     - INFO: ✓ Logged");
        System.out.println("     - WARN: ✓ Logged");
        System.out.println("     - ERROR: ✓ Logged");

        System.out.println("\n3. Parameterized logging (avoid string concatenation):");
        System.out.println("""
            // ✗ BAD - String concatenation always executed
            log.debug("User " + userId + " logged in at " + timestamp);

            // ✓ GOOD - Parameters only evaluated if DEBUG enabled
            log.debug("User {} logged in at {}", userId, timestamp);

            // Multiple parameters
            log.info("Order {} for user {} total: ${}", orderId, userId, total);

            // Exception logging (exception as last parameter)
            try {
                riskyOperation();
            } catch (Exception e) {
                log.error("Operation failed for user {}: {}", userId, e.getMessage(), e);
            }
            """);

        System.out.println("4. Conditional logging:");
        System.out.println("""
            // Check level before expensive operations
            if (log.isDebugEnabled()) {
                log.debug("Expensive debug info: {}", computeExpensiveInfo());
            }

            // Other level checks
            if (log.isTraceEnabled()) { }
            if (log.isInfoEnabled()) { }
            if (log.isWarnEnabled()) { }
            if (log.isErrorEnabled()) { }
            """);

        System.out.println();
    }

    // ========== Logback Configuration ==========

    private static void logbackConfigurationDemo() {
        System.out.println("--- Logback Configuration ---");
        System.out.println("Configure logging behavior\n");

        System.out.println("1. application.properties configuration:");
        System.out.println("""
            # Root log level
            logging.level.root=INFO

            # Package-specific levels
            logging.level.com.example.demo=DEBUG
            logging.level.org.springframework.web=DEBUG
            logging.level.org.hibernate=WARN

            # Log to file
            logging.file.name=application.log
            logging.file.path=/var/logs

            # Log file rotation
            logging.logback.rollingpolicy.max-file-size=10MB
            logging.logback.rollingpolicy.max-history=30
            logging.logback.rollingpolicy.total-size-cap=1GB

            # Log pattern
            logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
            logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n

            # Charset
            logging.charset.console=UTF-8
            logging.charset.file=UTF-8
            """);

        System.out.println("2. logback-spring.xml (advanced configuration):");
        System.out.println("""
            <?xml version="1.0" encoding="UTF-8"?>
            <configuration>

                <!-- Console appender -->
                <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
                    <encoder>
                        <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
                    </encoder>
                </appender>

                <!-- File appender with rolling policy -->
                <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
                    <file>logs/application.log</file>
                    <encoder>
                        <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
                    </encoder>
                    <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                        <!-- Daily rollover -->
                        <fileNamePattern>logs/application-%d{yyyy-MM-dd}.%i.log</fileNamePattern>
                        <timeBasedFileNamingAndTriggeringPolicy
                                class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                            <maxFileSize>10MB</maxFileSize>
                        </timeBasedFileNamingAndTriggeringPolicy>
                        <maxHistory>30</maxHistory>
                        <totalSizeCap>1GB</totalSizeCap>
                    </rollingPolicy>
                </appender>

                <!-- Async appender (better performance) -->
                <appender name="ASYNC_FILE" class="ch.qos.logback.classic.AsyncAppender">
                    <appender-ref ref="FILE"/>
                    <queueSize>512</queueSize>
                    <discardingThreshold>0</discardingThreshold>
                </appender>

                <!-- JSON appender (structured logging) -->
                <appender name="JSON_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
                    <file>logs/application.json</file>
                    <encoder class="net.logstash.logback.encoder.LogstashEncoder"/>
                    <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                        <fileNamePattern>logs/application-%d{yyyy-MM-dd}.json</fileNamePattern>
                    </rollingPolicy>
                </appender>

                <!-- Logger configuration -->
                <logger name="com.example.demo" level="DEBUG"/>
                <logger name="org.springframework.web" level="DEBUG"/>
                <logger name="org.hibernate" level="WARN"/>

                <!-- Root logger -->
                <root level="INFO">
                    <appender-ref ref="CONSOLE"/>
                    <appender-ref ref="ASYNC_FILE"/>
                </root>

                <!-- Profile-specific configuration -->
                <springProfile name="dev">
                    <logger name="com.example.demo" level="DEBUG"/>
                    <root level="DEBUG">
                        <appender-ref ref="CONSOLE"/>
                    </root>
                </springProfile>

                <springProfile name="prod">
                    <logger name="com.example.demo" level="INFO"/>
                    <root level="WARN">
                        <appender-ref ref="ASYNC_FILE"/>
                        <appender-ref ref="JSON_FILE"/>
                    </root>
                </springProfile>

            </configuration>
            """);

        System.out.println("3. Log pattern variables:");
        System.out.println("   %d{pattern}: Timestamp");
        System.out.println("   %thread: Thread name");
        System.out.println("   %-5level: Log level (padded to 5 chars)");
        System.out.println("   %logger{length}: Logger name (abbreviated)");
        System.out.println("   %msg: Log message");
        System.out.println("   %n: Line separator");
        System.out.println("   %ex: Exception stack trace");
        System.out.println("   %mdc{key}: MDC value");
        System.out.println("   %X{key}: MDC value (alternative)");

        System.out.println();
    }

    // ========== MDC (Mapped Diagnostic Context) ==========

    private static void mdcDemo() {
        System.out.println("--- MDC (Mapped Diagnostic Context) ---");
        System.out.println("Thread-local context for logging\n");

        System.out.println("1. Basic MDC usage:");
        System.out.println("""
            import org.slf4j.MDC;

            @Service
            public class UserService {

                private static final Logger log = LoggerFactory.getLogger(UserService.class);

                public void processUser(String userId) {
                    // Put value in MDC
                    MDC.put("userId", userId);
                    MDC.put("requestId", UUID.randomUUID().toString());

                    try {
                        log.info("Processing user");  // userId in context
                        doWork();
                    } finally {
                        // Always clean up MDC
                        MDC.remove("userId");
                        MDC.remove("requestId");
                        // Or clear all
                        MDC.clear();
                    }
                }
            }
            """);

        System.out.println("2. MDC in log pattern:");
        System.out.println("""
            # logback-spring.xml
            <pattern>%d [%thread] %-5level [%X{requestId}] [%X{userId}] %logger{36} - %msg%n</pattern>

            # Output example:
            2025-01-15 10:30:45 [http-nio-8080-exec-1] INFO  [a1b2c3] [user123] com.example.UserService - Processing user
            """);

        System.out.println("3. MDC with filter (web requests):");
        System.out.println("""
            @Component
            public class MdcFilter extends OncePerRequestFilter {

                @Override
                protected void doFilterInternal(HttpServletRequest request,
                                               HttpServletResponse response,
                                               FilterChain filterChain)
                        throws ServletException, IOException {

                    try {
                        // Add request context
                        MDC.put("requestId", UUID.randomUUID().toString());
                        MDC.put("method", request.getMethod());
                        MDC.put("uri", request.getRequestURI());
                        MDC.put("remoteAddr", request.getRemoteAddr());

                        // Get from header
                        String userId = request.getHeader("X-User-Id");
                        if (userId != null) {
                            MDC.put("userId", userId);
                        }

                        filterChain.doFilter(request, response);

                    } finally {
                        MDC.clear();
                    }
                }
            }
            """);

        System.out.println("4. MDC with CompletableFuture:");
        System.out.println("""
            public CompletableFuture<Result> asyncOperation() {
                // Capture MDC context
                Map<String, String> mdcContext = MDC.getCopyOfContextMap();

                return CompletableFuture.supplyAsync(() -> {
                    // Restore MDC in new thread
                    if (mdcContext != null) {
                        MDC.setContextMap(mdcContext);
                    }

                    try {
                        log.info("Async operation");  // MDC available
                        return doAsyncWork();
                    } finally {
                        MDC.clear();
                    }
                });
            }
            """);

        System.out.println();
    }

    // ========== Structured Logging ==========

    private static void structuredLoggingDemo() {
        System.out.println("--- Structured Logging ---");
        System.out.println("JSON logging for better log analysis\n");

        System.out.println("1. Add dependency:");
        System.out.println("""
            <dependency>
                <groupId>net.logstash.logback</groupId>
                <artifactId>logstash-logback-encoder</artifactId>
                <version>7.4</version>
            </dependency>
            """);

        System.out.println("2. Configure JSON appender:");
        System.out.println("""
            <!-- logback-spring.xml -->
            <appender name="JSON_CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
                <encoder class="net.logstash.logback.encoder.LogstashEncoder">
                    <includeMdcKeyName>requestId</includeMdcKeyName>
                    <includeMdcKeyName>userId</includeMdcKeyName>
                    <fieldNames>
                        <timestamp>@timestamp</timestamp>
                        <message>message</message>
                        <logger>logger_name</logger>
                        <thread>thread_name</thread>
                        <level>level</level>
                        <levelValue>[ignore]</levelValue>
                    </fieldNames>
                </encoder>
            </appender>
            """);

        System.out.println("3. Structured arguments:");
        System.out.println("""
            import static net.logstash.logback.argument.StructuredArguments.*;

            @Service
            public class OrderService {

                public void placeOrder(Order order) {
                    // Key-value pairs in JSON
                    log.info("Order placed",
                        keyValue("orderId", order.getId()),
                        keyValue("userId", order.getUserId()),
                        keyValue("total", order.getTotal()));

                    // Array
                    log.info("Items ordered",
                        array("items", order.getItems()));

                    // Nested object
                    log.info("Order details",
                        fields(order));
                }
            }

            // JSON output:
            {
              "@timestamp": "2025-01-15T10:30:45.123Z",
              "level": "INFO",
              "logger_name": "com.example.OrderService",
              "message": "Order placed",
              "orderId": "12345",
              "userId": "user123",
              "total": 99.99
            }
            """);

        System.out.println("4. Markers for filtering:");
        System.out.println("""
            import org.slf4j.Marker;
            import org.slf4j.MarkerFactory;

            public class SecurityService {

                private static final Marker SECURITY = MarkerFactory.getMarker("SECURITY");
                private static final Marker AUDIT = MarkerFactory.getMarker("AUDIT");

                public void login(String username) {
                    log.info(SECURITY, "User logged in: {}", username);
                }

                public void sensitiveOperation(String userId, String action) {
                    log.info(AUDIT, "Sensitive operation: {} by {}", action, userId);
                }
            }

            <!-- Filter by marker -->
            <appender name="SECURITY_FILE" class="ch.qos.logback.core.FileAppender">
                <file>logs/security.log</file>
                <filter class="ch.qos.logback.core.filter.EvaluatorFilter">
                    <evaluator>
                        <matcher>
                            <Name>SECURITY</Name>
                            <Value>SECURITY</Value>
                        </matcher>
                        <expression>marker != null &amp;&amp; marker.contains("SECURITY")</expression>
                    </evaluator>
                    <OnMatch>ACCEPT</OnMatch>
                    <OnMismatch>DENY</OnMismatch>
                </filter>
                <encoder>
                    <pattern>%d [%thread] %-5level %logger - %msg%n</pattern>
                </encoder>
            </appender>
            """);

        System.out.println();
    }

    // ========== Advanced Features ==========

    private static void advancedFeaturesDemo() {
        System.out.println("--- Advanced Logging Features ---");
        System.out.println("Performance, filtering, and best practices\n");

        System.out.println("1. Async logging (better performance):");
        System.out.println("""
            <appender name="ASYNC" class="ch.qos.logback.classic.AsyncAppender">
                <queueSize>512</queueSize>
                <discardingThreshold>0</discardingThreshold>  <!-- Don't discard events -->
                <neverBlock>false</neverBlock>  <!-- Block if queue full -->
                <appender-ref ref="FILE"/>
            </appender>

            <root level="INFO">
                <appender-ref ref="ASYNC"/>
            </root>
            """);

        System.out.println("2. Filtering by level:");
        System.out.println("""
            <appender name="ERROR_FILE" class="ch.qos.logback.core.FileAppender">
                <file>logs/error.log</file>
                <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
                    <level>ERROR</level>
                </filter>
                <encoder>
                    <pattern>%d [%thread] %-5level %logger - %msg%n</pattern>
                </encoder>
            </appender>
            """);

        System.out.println("3. Custom appender:");
        System.out.println("""
            public class SlackAppender extends AppenderBase<ILoggingEvent> {

                private String webhookUrl;

                @Override
                protected void append(ILoggingEvent event) {
                    if (event.getLevel().isGreaterOrEqual(Level.ERROR)) {
                        sendToSlack(event.getFormattedMessage());
                    }
                }

                private void sendToSlack(String message) {
                    // Send to Slack webhook
                }

                public void setWebhookUrl(String webhookUrl) {
                    this.webhookUrl = webhookUrl;
                }
            }

            <!-- Configure custom appender -->
            <appender name="SLACK" class="com.example.SlackAppender">
                <webhookUrl>https://hooks.slack.com/...</webhookUrl>
            </appender>
            """);

        System.out.println("4. Best practices:");
        System.out.println("   ✓ Use appropriate log levels");
        System.out.println("     - ERROR: Errors requiring attention");
        System.out.println("     - WARN: Potential issues, deprecations");
        System.out.println("     - INFO: Important business events");
        System.out.println("     - DEBUG: Detailed diagnostic (dev/staging)");
        System.out.println("     - TRACE: Very detailed (rarely needed)");
        System.out.println("\n   ✓ Use parameterized logging (not concatenation)");
        System.out.println("   ✓ Always log exceptions with stack trace");
        System.out.println("   ✓ Use MDC for request context");
        System.out.println("   ✓ Use async appenders in production");
        System.out.println("   ✓ Implement log rotation");
        System.out.println("   ✓ Use structured logging (JSON) for analysis");
        System.out.println("   ✓ Clean up MDC in finally blocks");
        System.out.println("   ✓ Don't log sensitive data (passwords, tokens)");
        System.out.println("\n   ✗ Don't log at INFO level in tight loops");
        System.out.println("   ✗ Don't log large objects without sanitization");
        System.out.println("   ✗ Don't use System.out.println in production code");
        System.out.println("   ✗ Don't log and rethrow exceptions (log once)");

        System.out.println();
    }

    // ========== Log4j 2 Alternative ==========

    private static void log4j2AlternativeDemo() {
        System.out.println("--- Log4j 2 (Alternative to Logback) ---");
        System.out.println("How to use Log4j 2 instead of Logback\n");

        System.out.println("1. Replace Logback with Log4j 2:");
        System.out.println("""
            <dependencies>
                <!-- Exclude Spring Boot's default logging -->
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter</artifactId>
                    <exclusions>
                        <exclusion>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter-logging</artifactId>
                        </exclusion>
                    </exclusions>
                </dependency>

                <!-- Add Log4j 2 -->
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-log4j2</artifactId>
                </dependency>
            </dependencies>
            """);

        System.out.println("2. log4j2-spring.xml configuration:");
        System.out.println("""
            <?xml version="1.0" encoding="UTF-8"?>
            <Configuration status="WARN">
                <Appenders>
                    <Console name="Console" target="SYSTEM_OUT">
                        <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n"/>
                    </Console>

                    <RollingFile name="RollingFile" fileName="logs/app.log"
                                filePattern="logs/app-%d{yyyy-MM-dd}-%i.log">
                        <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n"/>
                        <Policies>
                            <TimeBasedTriggeringPolicy interval="1"/>
                            <SizeBasedTriggeringPolicy size="10MB"/>
                        </Policies>
                        <DefaultRolloverStrategy max="30"/>
                    </RollingFile>
                </Appenders>

                <Loggers>
                    <Logger name="com.example.demo" level="debug" additivity="false">
                        <AppenderRef ref="Console"/>
                        <AppenderRef ref="RollingFile"/>
                    </Logger>

                    <Root level="info">
                        <AppenderRef ref="Console"/>
                        <AppenderRef ref="RollingFile"/>
                    </Root>
                </Loggers>
            </Configuration>
            """);

        System.out.println("3. Log4j 2 advantages:");
        System.out.println("   ✓ Better async performance (LMAX Disruptor)");
        System.out.println("   ✓ Garbage-free logging");
        System.out.println("   ✓ Automatic configuration reloading");
        System.out.println("   ✓ Plugin architecture");
        System.out.println("   ✓ Lambda support for lazy evaluation");

        System.out.println("\n4. Note on Log4j 1.x:");
        System.out.println("   ✗ Log4j 1.x is DEPRECATED (end-of-life)");
        System.out.println("   ✗ Has known security vulnerabilities");
        System.out.println("   ✗ DO NOT use in new projects");
        System.out.println("   ✓ Use Log4j 2 or Logback instead");

        System.out.println();
    }
}
