package com.example.demo.showcase;

/**
 * Demonstrates Apache Camel integration framework
 * Covers routes, endpoints, EIP patterns, transformations, and error handling
 *
 * Note: This is a demonstration - actual routes are not started
 */
public class ApacheCamelShowcase {

    public static void demonstrate() {
        System.out.println("\n========== APACHE CAMEL SHOWCASE ==========\n");

        camelOverviewDemo();
        routesDemo();
        eipPatternsDemo();
        transformationDemo();
        errorHandlingDemo();
        componentsDemo();
        advancedFeaturesDemo();
    }

    // ========== Overview ==========

    private static void camelOverviewDemo() {
        System.out.println("--- Apache Camel Overview ---");
        System.out.println("Integration framework based on Enterprise Integration Patterns\n");

        System.out.println("1. Core concepts:");
        System.out.println("   Route: Path that messages follow");
        System.out.println("   Endpoint: Channel to send/receive messages (URI-based)");
        System.out.println("   Component: Provides endpoints (file, http, jms, kafka, etc.)");
        System.out.println("   Processor: Custom message processing logic");
        System.out.println("   Exchange: Container for message (in, out, properties)");
        System.out.println("   Message: Body, headers, attachments");

        System.out.println("\n2. Route syntax:");
        System.out.println("   from(\"source\").to(\"destination\")");
        System.out.println("   DSL (Domain Specific Language) for routing");

        System.out.println("\n3. Dependencies:");
        System.out.println("""
            <dependency>
                <groupId>org.apache.camel.springboot</groupId>
                <artifactId>camel-spring-boot-starter</artifactId>
                <version>4.3.0</version>
            </dependency>
            """);

        System.out.println();
    }

    // ========== Routes ==========

    private static void routesDemo() {
        System.out.println("--- Camel Routes ---");
        System.out.println("Define message routing logic\n");

        System.out.println("1. Basic route:");
        System.out.println("""
            @Component
            public class FileRoute extends RouteBuilder {

                @Override
                public void configure() throws Exception {
                    // File to file copy
                    from("file:input?noop=true")
                        .to("file:output");

                    // Every 5 seconds, log message
                    from("timer:myTimer?period=5000")
                        .log("Timer fired at ${date:now:yyyy-MM-dd HH:mm:ss}");

                    // HTTP endpoint
                    from("direct:processOrder")
                        .log("Processing order: ${body}")
                        .to("http://api.example.com/orders");
                }
            }
            """);

        System.out.println("2. Multiple destinations:");
        System.out.println("""
            @Override
            public void configure() throws Exception {
                // Send to multiple destinations (multicast)
                from("direct:broadcast")
                    .multicast()
                        .to("log:destination1")
                        .to("log:destination2")
                        .to("jms:queue:orders");

                // Pipeline (sequential processing)
                from("direct:pipeline")
                    .to("direct:step1")
                    .to("direct:step2")
                    .to("direct:step3");
            }
            """);

        System.out.println("3. Content-based routing:");
        System.out.println("""
            @Override
            public void configure() throws Exception {
                from("direct:orders")
                    .choice()
                        .when(simple("${header.priority} == 'HIGH'"))
                            .to("jms:queue:highPriority")
                        .when(simple("${header.priority} == 'MEDIUM'"))
                            .to("jms:queue:mediumPriority")
                        .otherwise()
                            .to("jms:queue:lowPriority")
                    .end();

                // Based on body content
                from("direct:messages")
                    .choice()
                        .when(body().contains("ERROR"))
                            .to("log:errors?level=ERROR")
                        .when(body().contains("WARN"))
                            .to("log:warnings?level=WARN")
                        .otherwise()
                            .to("log:info?level=INFO")
                    .end();
            }
            """);

        System.out.println("4. Message filtering:");
        System.out.println("""
            @Override
            public void configure() throws Exception {
                // Filter by header
                from("direct:input")
                    .filter(header("type").isEqualTo("ORDER"))
                        .to("jms:queue:orders");

                // Filter by body
                from("direct:messages")
                    .filter(body().contains("Important"))
                        .to("jms:queue:important");

                // Filter with simple expression
                from("direct:numbers")
                    .filter(simple("${body} > 100"))
                        .to("log:largeNumbers");
            }
            """);

        System.out.println("5. Wiretap (copy to another route):");
        System.out.println("""
            @Override
            public void configure() throws Exception {
                // Send copy to monitoring, continue with original
                from("direct:orders")
                    .wireTap("direct:monitoring")
                    .to("jms:queue:orderProcessing");

                from("direct:monitoring")
                    .to("log:audit");
            }
            """);

        System.out.println();
    }

    // ========== EIP Patterns ==========

    private static void eipPatternsDemo() {
        System.out.println("--- Enterprise Integration Patterns ---");
        System.out.println("Common messaging patterns in Camel\n");

        System.out.println("1. Splitter (split message into parts):");
        System.out.println("""
            @Override
            public void configure() throws Exception {
                // Split CSV lines
                from("file:input?fileName=data.csv")
                    .split(body().tokenize("\\n"))
                        .to("jms:queue:processLine");

                // Split JSON array
                from("direct:jsonArray")
                    .unmarshal().json()
                    .split(simple("${body}"))
                        .to("direct:processItem");

                // Split with aggregation
                from("direct:orders")
                    .split(body(), new OrderAggregationStrategy())
                        .to("direct:processOrder")
                    .end()
                    .to("direct:allProcessed");
            }
            """);

        System.out.println("2. Aggregator (combine messages):");
        System.out.println("""
            @Override
            public void configure() throws Exception {
                // Aggregate by correlation key
                from("direct:items")
                    .aggregate(header("orderId"), new MyAggregationStrategy())
                        .completionSize(10)  // Complete after 10 items
                        .completionTimeout(5000)  // Or after 5 seconds
                        .to("direct:processAggregatedOrder");

                // Aggregate all
                from("direct:batch")
                    .aggregate(constant(true), new BatchAggregationStrategy())
                        .completionInterval(10000)  // Every 10 seconds
                        .to("jms:queue:batchProcessing");
            }

            // Custom aggregation strategy
            public class MyAggregationStrategy implements AggregationStrategy {
                @Override
                public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
                    if (oldExchange == null) {
                        return newExchange;
                    }
                    String oldBody = oldExchange.getIn().getBody(String.class);
                    String newBody = newExchange.getIn().getBody(String.class);
                    oldExchange.getIn().setBody(oldBody + "," + newBody);
                    return oldExchange;
                }
            }
            """);

        System.out.println("3. Content Enricher:");
        System.out.println("""
            @Override
            public void configure() throws Exception {
                // Enrich with external data
                from("direct:orders")
                    .enrich("direct:getUserDetails", (oldExchange, newExchange) -> {
                        String order = oldExchange.getIn().getBody(String.class);
                        String userDetails = newExchange.getIn().getBody(String.class);
                        oldExchange.getIn().setBody(order + " enriched with " + userDetails);
                        return oldExchange;
                    })
                    .to("jms:queue:enrichedOrders");

                // Enrich from HTTP
                from("direct:enrichFromApi")
                    .enrich("http://api.example.com/user/${header.userId}")
                    .to("direct:processEnrichedData");
            }
            """);

        System.out.println("4. Recipient List (dynamic routing):");
        System.out.println("""
            @Override
            public void configure() throws Exception {
                // Route to multiple destinations from header
                from("direct:notify")
                    .recipientList(header("recipients"));

                // Destinations in header: "jms:queue:email,jms:queue:sms,jms:queue:push"

                // Dynamic based on logic
                from("direct:route")
                    .recipientList(method(RoutingBean.class, "determineDestinations"));
            }

            public class RoutingBean {
                public String determineDestinations(@Body String message) {
                    if (message.contains("urgent")) {
                        return "jms:queue:urgent,sms:service";
                    }
                    return "jms:queue:normal";
                }
            }
            """);

        System.out.println("5. Load Balancer:");
        System.out.println("""
            @Override
            public void configure() throws Exception {
                // Round robin
                from("direct:input")
                    .loadBalance().roundRobin()
                        .to("direct:server1")
                        .to("direct:server2")
                        .to("direct:server3");

                // Random
                from("direct:random")
                    .loadBalance().random()
                        .to("direct:serverA")
                        .to("direct:serverB");

                // Failover (try next if fails)
                from("direct:failover")
                    .loadBalance().failover()
                        .to("http://server1/api")
                        .to("http://server2/api")
                        .to("http://server3/api");
            }
            """);

        System.out.println("6. Throttler (rate limiting):");
        System.out.println("""
            @Override
            public void configure() throws Exception {
                // Limit to 10 messages per second
                from("direct:input")
                    .throttle(10).timePeriodMillis(1000)
                    .to("http://api.example.com");

                // Limit to 100 per minute
                from("jms:queue:requests")
                    .throttle(100).timePeriodMillis(60000)
                    .to("direct:process");
            }
            """);

        System.out.println();
    }

    // ========== Transformation ==========

    private static void transformationDemo() {
        System.out.println("--- Message Transformation ---");
        System.out.println("Transform message body and headers\n");

        System.out.println("1. Simple transformations:");
        System.out.println("""
            @Override
            public void configure() throws Exception {
                // Set body
                from("direct:start")
                    .setBody(constant("Hello World"))
                    .to("log:output");

                // Transform body
                from("direct:transform")
                    .transform(simple("Transformed: ${body}"))
                    .to("log:output");

                // Set header
                from("direct:headers")
                    .setHeader("myHeader", constant("value"))
                    .setHeader("timestamp", simple("${date:now:yyyy-MM-dd}"))
                    .to("log:output");

                // Remove header
                from("direct:removeHeader")
                    .removeHeader("unwantedHeader")
                    .to("log:output");
            }
            """);

        System.out.println("2. JSON/XML marshalling:");
        System.out.println("""
            @Override
            public void configure() throws Exception {
                // Object to JSON
                from("direct:objectToJson")
                    .marshal().json(JsonLibrary.Jackson)
                    .to("jms:queue:jsonMessages");

                // JSON to Object
                from("jms:queue:jsonMessages")
                    .unmarshal().json(JsonLibrary.Jackson, Order.class)
                    .to("direct:processOrder");

                // Object to XML
                from("direct:objectToXml")
                    .marshal().jaxb()
                    .to("file:output?fileName=order.xml");

                // XML to Object
                from("file:input?fileName=order.xml")
                    .unmarshal().jaxb()
                    .to("direct:processOrder");
            }
            """);

        System.out.println("3. Data transformation with processor:");
        System.out.println("""
            @Override
            public void configure() throws Exception {
                from("direct:customTransform")
                    .process(exchange -> {
                        String body = exchange.getIn().getBody(String.class);
                        String transformed = body.toUpperCase();
                        exchange.getIn().setBody(transformed);
                    })
                    .to("log:output");

                // Bean method
                from("direct:beanTransform")
                    .bean(TransformBean.class, "transform")
                    .to("log:output");
            }

            public class TransformBean {
                public String transform(String input) {
                    return input.toUpperCase();
                }
            }
            """);

        System.out.println("4. Type conversion:");
        System.out.println("""
            @Override
            public void configure() throws Exception {
                from("direct:convert")
                    .convertBodyTo(String.class)
                    .to("log:stringBody");

                from("direct:convertToInt")
                    .convertBodyTo(Integer.class)
                    .to("log:intBody");

                // Convert header
                from("direct:convertHeader")
                    .setHeader("count").convertTo(Integer.class)
                    .to("log:output");
            }
            """);

        System.out.println();
    }

    // ========== Error Handling ==========

    private static void errorHandlingDemo() {
        System.out.println("--- Error Handling ---");
        System.out.println("Handle failures and retries\n");

        System.out.println("1. Error handler:");
        System.out.println("""
            @Override
            public void configure() throws Exception {
                // Default error handler (no retry)
                errorHandler(defaultErrorHandler());

                // Dead letter channel
                errorHandler(deadLetterChannel("jms:queue:errors")
                    .maximumRedeliveries(3)
                    .redeliveryDelay(1000)
                    .logStackTrace(true));

                // No error handler
                errorHandler(noErrorHandler());
            }
            """);

        System.out.println("2. onException:");
        System.out.println("""
            @Override
            public void configure() throws Exception {
                // Handle specific exception
                onException(IOException.class)
                    .maximumRedeliveries(3)
                    .redeliveryDelay(1000)
                    .log("IOException occurred: ${exception.message}")
                    .handled(true)  // Mark as handled
                    .to("jms:queue:ioErrors");

                // Handle with retry
                onException(TimeoutException.class)
                    .maximumRedeliveries(5)
                    .backOffMultiplier(2)  // Exponential backoff
                    .log("Timeout, retrying...")
                    .retryAttemptedLogLevel(LoggingLevel.WARN);

                // Continue route after exception
                onException(ValidationException.class)
                    .handled(true)
                    .log("Validation failed: ${exception.message}")
                    .transform(constant("Validation failed"))
                    .to("direct:validationError");

                from("direct:process")
                    .to("http://api.example.com");
            }
            """);

        System.out.println("3. doTry/doCatch/doFinally:");
        System.out.println("""
            @Override
            public void configure() throws Exception {
                from("direct:tryCatch")
                    .doTry()
                        .to("http://api.example.com")
                        .to("jms:queue:success")
                    .doCatch(IOException.class)
                        .log("Network error: ${exception.message}")
                        .to("jms:queue:networkErrors")
                    .doCatch(Exception.class)
                        .log("General error: ${exception.message}")
                        .to("jms:queue:errors")
                    .doFinally()
                        .log("Processing completed")
                    .end();
            }
            """);

        System.out.println("4. onCompletion:");
        System.out.println("""
            @Override
            public void configure() throws Exception {
                // Execute on successful completion
                onCompletion().onCompleteOnly()
                    .log("Route completed successfully")
                    .to("jms:queue:success");

                // Execute on failure
                onCompletion().onFailureOnly()
                    .log("Route failed: ${exception.message}")
                    .to("jms:queue:failures");

                from("direct:process")
                    .to("http://api.example.com");
            }
            """);

        System.out.println();
    }

    // ========== Components ==========

    private static void componentsDemo() {
        System.out.println("--- Camel Components ---");
        System.out.println("Built-in components for various protocols\n");

        System.out.println("1. File component:");
        System.out.println("""
            from("file:input?delete=true")  // Delete after processing
            from("file:input?noop=true")    // Leave file in place
            from("file:input?move=processed")  // Move to processed folder
            from("file:input?include=.*\\.csv")  // Only CSV files
            """);

        System.out.println("\n2. HTTP component:");
        System.out.println("""
            from("direct:httpGet")
                .to("http://api.example.com/users?bridgeEndpoint=true")
                .to("log:response");

            from("direct:httpPost")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("http://api.example.com/users");
            """);

        System.out.println("\n3. JMS component:");
        System.out.println("""
            from("jms:queue:orders")
                .to("direct:processOrder");

            from("direct:sendToQueue")
                .to("jms:queue:notifications");

            // Topic (pub/sub)
            from("jms:topic:events")
                .to("log:events");
            """);

        System.out.println("\n4. Kafka component:");
        System.out.println("""
            from("kafka:orders?brokers=localhost:9092&groupId=myGroup")
                .to("direct:processOrder");

            from("direct:publishToKafka")
                .to("kafka:notifications?brokers=localhost:9092");
            """);

        System.out.println("\n5. Database component:");
        System.out.println("""
            from("sql:select * from orders where processed = false?" +
                 "dataSource=#dataSource&" +
                 "onConsume=update orders set processed = true where id = :#id")
                .to("direct:processOrder");
            """);

        System.out.println("\n6. REST component:");
        System.out.println("""
            @Override
            public void configure() throws Exception {
                rest("/api")
                    .get("/users/{id}")
                        .to("direct:getUser")
                    .post("/users")
                        .type(User.class)
                        .to("direct:createUser")
                    .delete("/users/{id}")
                        .to("direct:deleteUser");

                from("direct:getUser")
                    .to("sql:select * from users where id = :#id");
            }
            """);

        System.out.println("\n7. Direct/SEDA components:");
        System.out.println("""
            // Direct: Synchronous in-memory
            from("direct:start")
                .to("direct:process");

            // SEDA: Asynchronous with queue
            from("seda:async?concurrentConsumers=5")
                .to("direct:process");
            """);

        System.out.println();
    }

    // ========== Advanced Features ==========

    private static void advancedFeaturesDemo() {
        System.out.println("--- Advanced Camel Features ---");
        System.out.println("Transactions, testing, and best practices\n");

        System.out.println("1. Transactions:");
        System.out.println("""
            @Override
            public void configure() throws Exception {
                from("jms:queue:orders?transacted=true")
                    .transacted()
                    .to("sql:insert into orders...")
                    .to("jms:queue:notifications")
                    .to("http://api.example.com");
                // All or nothing - rollback on failure
            }
            """);

        System.out.println("2. Idempotent consumer:");
        System.out.println("""
            @Override
            public void configure() throws Exception {
                from("jms:queue:orders")
                    .idempotentConsumer(
                        header("orderId"),
                        MemoryIdempotentRepository.memoryIdempotentRepository(200)
                    )
                    .to("direct:processOrder");
                // Duplicate messages with same orderId are skipped
            }
            """);

        System.out.println("3. Route policies:");
        System.out.println("""
            @Override
            public void configure() throws Exception {
                // Schedule route
                from("file:input")
                    .routePolicy(new CronScheduledRoutePolicy())
                    .startSchedule("0 0 * * * ?")  // Every hour
                    .to("direct:process");

                // Throttle policy
                ThrottlingInflightRoutePolicy policy =
                    new ThrottlingInflightRoutePolicy();
                policy.setMaxInflightExchanges(10);

                from("jms:queue:orders")
                    .routePolicy(policy)
                    .to("direct:process");
            }
            """);

        System.out.println("4. Testing routes:");
        System.out.println("""
            @SpringBootTest
            public class MyRouteTest {

                @Autowired
                private CamelContext camelContext;

                @Autowired
                private ProducerTemplate producerTemplate;

                @Test
                public void testRoute() throws Exception {
                    // Send message
                    producerTemplate.sendBody("direct:start", "Test message");

                    // Mock endpoints
                    MockEndpoint mockEndpoint = camelContext
                        .getEndpoint("mock:result", MockEndpoint.class);

                    mockEndpoint.expectedMessageCount(1);
                    mockEndpoint.expectedBodiesReceived("Transformed: Test message");

                    mockEndpoint.assertIsSatisfied();
                }
            }
            """);

        System.out.println("5. Best practices:");
        System.out.println("   ✓ Use direct: for synchronous in-VM calls");
        System.out.println("   ✓ Use seda: for async in-VM with queue");
        System.out.println("   ✓ Always handle exceptions (onException, doTry)");
        System.out.println("   ✓ Use error handlers with dead letter queue");
        System.out.println("   ✓ Implement idempotent consumers for duplicate protection");
        System.out.println("   ✓ Use transactions for critical operations");
        System.out.println("   ✓ Test routes with CamelTestSupport");
        System.out.println("   ✓ Monitor routes with JMX/Actuator");
        System.out.println("   ✓ Use wiretap for auditing");
        System.out.println("   ✗ Don't create too many routes (performance)");
        System.out.println("   ✗ Don't ignore error handling");

        System.out.println();
    }
}
