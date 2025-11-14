package com.example.demo.showcase;

/**
 * Demonstrates Solace PubSub+ messaging platform
 * Covers pub/sub messaging, queues, topics, guaranteed delivery, and request-reply
 */
public class SolaceShowcase {

    public static void demonstrate() {
        System.out.println("\n========== SOLACE PUBSUB+ SHOWCASE ==========\n");

        solaceOverviewDemo();
        directMessagingDemo();
        guaranteedMessagingDemo();
        requestReplyDemo();
        queueAndTopicDemo();
        advancedFeaturesDemo();
    }

    // ========== Overview ==========

    private static void solaceOverviewDemo() {
        System.out.println("--- Solace PubSub+ Overview ---");
        System.out.println("Enterprise messaging platform with pub/sub, queuing, and streaming\n");

        System.out.println("1. Key features:");
        System.out.println("   • Publish-subscribe messaging");
        System.out.println("   • Queues with load balancing");
        System.out.println("   • Direct (best-effort) and guaranteed (persistent) messaging");
        System.out.println("   • Request-reply pattern");
        System.out.println("   • Topic hierarchy and wildcards");
        System.out.println("   • Multi-protocol support (AMQP, MQTT, REST, WebSocket)");
        System.out.println("   • High throughput and low latency");

        System.out.println("\n2. Dependencies:");
        System.out.println("""
            <dependency>
                <groupId>com.solace.spring.boot</groupId>
                <artifactId>solace-spring-boot-starter</artifactId>
                <version>1.2.2</version>
            </dependency>

            <!-- Additional Solace Cloud dependency if needed -->
            <dependency>
                <groupId>com.solacesystems</groupId>
                <artifactId>sol-jcsmp</artifactId>
                <version>10.21.0</version>
            </dependency>
            """);

        System.out.println("3. Configuration:");
        System.out.println("""
            # application.properties

            # Solace connection
            solace.java.host=tcp://localhost:55555
            solace.java.msgVpn=default
            solace.java.clientUsername=default
            solace.java.clientPassword=default

            # Or Solace Cloud
            solace.java.host=tcps://mr-connection-xxx.messaging.solace.cloud:55443
            solace.java.msgVpn=myVpn
            solace.java.clientUsername=solace-cloud-client
            solace.java.clientPassword=password123

            # Connection pooling
            solace.java.connectRetries=3
            solace.java.reconnectRetries=-1
            solace.java.connectRetriesPerHost=5
            """);

        System.out.println();
    }

    // ========== Direct Messaging ==========

    private static void directMessagingDemo() {
        System.out.println("--- Direct Messaging (Best-Effort) ---");
        System.out.println("Low-latency, non-persistent messaging\n");

        System.out.println("1. Publisher:");
        System.out.println("""
            import com.solace.spring.cloud.stream.binder.messaging.SolaceHeaders;
            import org.springframework.messaging.Message;
            import org.springframework.messaging.support.MessageBuilder;

            @Service
            public class EventPublisher {

                @Autowired
                private SolaceTemplate solaceTemplate;

                public void publishEvent(String eventData) {
                    // Simple publish
                    solaceTemplate.send("event/user/created", eventData);
                }

                public void publishWithHeaders(UserEvent event) {
                    Message<UserEvent> message = MessageBuilder
                        .withPayload(event)
                        .setHeader("eventType", "USER_CREATED")
                        .setHeader("timestamp", System.currentTimeMillis())
                        .setHeader(SolaceHeaders.PRIORITY, 4)
                        .build();

                    solaceTemplate.send("event/user/created", message);
                }

                public void publishToMultipleTopics(String event) {
                    // Hierarchical topics
                    solaceTemplate.send("analytics/page/view", event);
                    solaceTemplate.send("analytics/user/login", event);
                    solaceTemplate.send("analytics/purchase/completed", event);
                }
            }
            """);

        System.out.println("2. Subscriber:");
        System.out.println("""
            import com.solace.spring.boot.autoconfigure.SolaceJavaAutoConfiguration;

            @Component
            public class EventSubscriber {

                @SolaceMessageHandler(destinations = "event/user/created")
                public void handleUserCreated(String eventData) {
                    System.out.println("User created: " + eventData);
                }

                @SolaceMessageHandler(destinations = "event/user/>")  // Wildcard
                public void handleAllUserEvents(String eventData,
                    @Header("eventType") String eventType) {
                    System.out.println("User event [" + eventType + "]: " + eventData);
                }

                @SolaceMessageHandler(destinations = "analytics/*/*/*")  // Multi-level wildcard
                public void handleAnalytics(String eventData,
                    @DestinationVariable String category,
                    @DestinationVariable String action,
                    @DestinationVariable String status) {
                    System.out.println("Analytics: " + category + "/" +
                        action + "/" + status);
                }
            }
            """);

        System.out.println("3. Topic wildcards:");
        System.out.println("""
            Topic patterns:
            • "event/user/created"           - Exact match
            • "event/user/*"                 - Single-level wildcard (user/created, user/updated)
            • "event/user/>"                 - Multi-level wildcard (user/created, user/profile/updated)
            • "event/*/created"              - Wildcard in middle position
            • "analytics/*/*/completed"      - Multiple wildcards

            Examples:
            • "order/>" subscribes to:
              - order/created
              - order/shipped
              - order/payment/completed
              - order/items/added

            • "*/status" subscribes to:
              - order/status
              - user/status
              - payment/status
            """);

        System.out.println();
    }

    // ========== Guaranteed Messaging ==========

    private static void guaranteedMessagingDemo() {
        System.out.println("--- Guaranteed Messaging (Persistent) ---");
        System.out.println("Reliable message delivery with persistence\n");

        System.out.println("1. Publisher with acknowledgment:");
        System.out.println("""
            import com.solacesystems.jcsmp.*;

            @Service
            public class OrderPublisher {

                @Autowired
                private JCSMPSession session;

                public void publishGuaranteedMessage(Order order) throws JCSMPException {
                    // Create producer
                    XMLMessageProducer producer = session.getMessageProducer(
                        new JCSMPStreamingPublishEventHandler() {
                            @Override
                            public void responseReceived(String messageID) {
                                System.out.println("Message acknowledged: " + messageID);
                            }

                            @Override
                            public void handleError(String messageID,
                                                   JCSMPException e,
                                                   long timestamp) {
                                System.err.println("Message failed: " + messageID);
                            }
                        }
                    );

                    // Create message
                    Topic topic = JCSMPFactory.onlyInstance()
                        .createTopic("order/created");

                    TextMessage message = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
                    message.setText(toJson(order));
                    message.setDeliveryMode(DeliveryMode.PERSISTENT);
                    message.setApplicationMessageId(order.getId());

                    // Send and wait for acknowledgment
                    producer.send(message, topic);
                }
            }
            """);

        System.out.println("2. Queue consumer:");
        System.out.println("""
            @Service
            public class OrderConsumer {

                @Autowired
                private JCSMPSession session;

                @PostConstruct
                public void init() throws JCSMPException {
                    Queue queue = JCSMPFactory.onlyInstance()
                        .createQueue("orderQueue");

                    FlowReceiver flowReceiver = session.createFlow(
                        new XMLMessageListener() {
                            @Override
                            public void onReceive(BytesXMLMessage message) {
                                if (message instanceof TextMessage) {
                                    String text = ((TextMessage) message).getText();
                                    processOrder(text);

                                    // Acknowledge message
                                    message.ackMessage();
                                }
                            }

                            @Override
                            public void onException(JCSMPException e) {
                                System.err.println("Consumer exception: " + e);
                            }
                        },
                        new ConsumerFlowProperties()
                            .setEndpoint(queue)
                            .setAckMode(JCSMPProperties.SUPPORTED_MESSAGE_ACK_CLIENT)
                    );

                    flowReceiver.start();
                }
            }
            """);

        System.out.println("3. Spring Cloud Stream integration:");
        System.out.println("""
            @Configuration
            public class SolaceBindings {

                @Bean
                public Consumer<Message<Order>> orderConsumer() {
                    return message -> {
                        Order order = message.getPayload();
                        System.out.println("Processing order: " + order.getId());

                        // Message is automatically acknowledged after successful processing
                    };
                }

                @Bean
                public Supplier<Message<Order>> orderSupplier() {
                    return () -> MessageBuilder
                        .withPayload(new Order())
                        .setHeader(SolaceHeaders.IS_PERSISTENT, true)
                        .build();
                }
            }

            # application.yml
            spring:
              cloud:
                stream:
                  bindings:
                    orderConsumer-in-0:
                      destination: orderQueue
                      group: order-processing-group
                    orderSupplier-out-0:
                      destination: order/created
                  solace:
                    bindings:
                      orderConsumer-in-0:
                        consumer:
                          queueAccessType: NON_EXCLUSIVE
                          provisionDurableQueue: true
            """);

        System.out.println();
    }

    // ========== Request-Reply ==========

    private static void requestReplyDemo() {
        System.out.println("--- Request-Reply Pattern ---");
        System.out.println("Synchronous messaging with automatic correlation\n");

        System.out.println("1. Requestor:");
        System.out.println("""
            @Service
            public class UserServiceClient {

                @Autowired
                private SolaceTemplate solaceTemplate;

                public User getUserById(Long userId) {
                    // Send request and wait for reply
                    Message<Long> request = MessageBuilder
                        .withPayload(userId)
                        .build();

                    Message<?> reply = solaceTemplate.sendAndReceive(
                        "user/query",
                        request,
                        5000  // 5 second timeout
                    );

                    return (User) reply.getPayload();
                }

                public CompletableFuture<User> getUserAsync(Long userId) {
                    Message<Long> request = MessageBuilder
                        .withPayload(userId)
                        .build();

                    return solaceTemplate.asyncSendAndReceive(
                        "user/query",
                        request,
                        5000
                    ).thenApply(reply -> (User) reply.getPayload());
                }
            }
            """);

        System.out.println("2. Replier:");
        System.out.println("""
            @Component
            public class UserServiceReplier {

                @Autowired
                private UserRepository userRepository;

                @SolaceMessageHandler(destinations = "user/query")
                @SendTo  // Reply to JMSReplyTo destination
                public User handleUserQuery(Long userId) {
                    return userRepository.findById(userId)
                        .orElseThrow(() -> new NotFoundException("User not found"));
                }

                // Or manual reply
                @SolaceMessageHandler(destinations = "user/query")
                public void handleUserQueryManual(Long userId,
                    @Header(SolaceHeaders.REPLY_TO) String replyTo) {

                    User user = userRepository.findById(userId).orElse(null);

                    Message<User> reply = MessageBuilder
                        .withPayload(user)
                        .setHeader(SolaceHeaders.CORRELATION_ID, getCorrelationId())
                        .build();

                    solaceTemplate.send(replyTo, reply);
                }
            }
            """);

        System.out.println();
    }

    // ========== Queue and Topic Endpoints ==========

    private static void queueAndTopicDemo() {
        System.out.println("--- Queues and Topic Endpoints ---");
        System.out.println("Load balancing and message filtering\n");

        System.out.println("1. Queue configuration:");
        System.out.println("""
            Queue characteristics:
            • Exclusive: Single consumer (primary/backup failover)
            • Non-exclusive: Multiple consumers (load balancing)
            • Durable: Survives broker restart
            • Topic subscription: Queue subscribes to topics

            # Solace broker configuration
            Queue: orderQueue
              Subscriptions:
                - order/created
                - order/updated
              Access Type: Non-Exclusive
              Max Message Count: 100,000
              Max TTL: 86400 seconds (1 day)
            """);

        System.out.println("2. Multiple consumers (load balancing):");
        System.out.println("""
            @Component
            public class OrderProcessor1 {
                @SolaceMessageHandler(destinations = "orderQueue")
                public void processOrder(Order order) {
                    System.out.println("Processor 1 handling: " + order.getId());
                }
            }

            @Component
            public class OrderProcessor2 {
                @SolaceMessageHandler(destinations = "orderQueue")
                public void processOrder(Order order) {
                    System.out.println("Processor 2 handling: " + order.getId());
                }
            }

            // Messages round-robin between consumers
            """);

        System.out.println("3. Topic Endpoint (Durable Topic Endpoint):");
        System.out.println("""
            // Topic Endpoint = Queue + Topic subscription
            // Combines pub/sub with reliable delivery

            @Bean
            public Consumer<Message<Event>> analyticsProcessor() {
                return message -> {
                    Event event = message.getPayload();
                    processAnalytics(event);
                };
            }

            # application.yml
            spring:
              cloud:
                stream:
                  bindings:
                    analyticsProcessor-in-0:
                      destination: analytics/>  # Topic subscription
                      group: analytics-group    # Creates durable queue
                  solace:
                    bindings:
                      analyticsProcessor-in-0:
                        consumer:
                          queueAccessType: EXCLUSIVE
                          addSubscriptions: true
            """);

        System.out.println();
    }

    // ========== Advanced Features ==========

    private static void advancedFeaturesDemo() {
        System.out.println("--- Advanced Features ---");
        System.out.println("Error handling, DMQ, transaction, and best practices\n");

        System.out.println("1. Error handling:");
        System.out.println("""
            @Component
            public class RobustConsumer {

                @SolaceMessageHandler(destinations = "orderQueue")
                public void processOrder(Order order, Acknowledgment acknowledgment) {
                    try {
                        processBusinessLogic(order);
                        acknowledgment.acknowledge();  // Manual ACK

                    } catch (RetryableException e) {
                        // Reject and requeue
                        acknowledgment.nack(Duration.ofSeconds(10));  // Retry after delay

                    } catch (FatalException e) {
                        // Reject without requeue (goes to DMQ)
                        acknowledgment.nack(Duration.ZERO);
                        logError(order, e);
                    }
                }
            }

            @Component
            public class ErrorHandler {

                @SolaceMessageHandler(destinations = "#DMQ")  // Dead Message Queue
                public void handleDeadMessages(Message<?> message) {
                    System.err.println("Dead message: " + message);
                    // Alert, log to database, manual intervention
                }
            }
            """);

        System.out.println("2. Message properties:");
        System.out.println("""
            Message<Order> message = MessageBuilder
                .withPayload(order)
                // Delivery settings
                .setHeader(SolaceHeaders.IS_PERSISTENT, true)
                .setHeader(SolaceHeaders.PRIORITY, 4)  // 0-255
                .setHeader(SolaceHeaders.TIME_TO_LIVE, 60000L)  // 60 seconds

                // Application headers
                .setHeader("orderId", order.getId())
                .setHeader("customerId", order.getCustomerId())
                .setHeader("version", "1.0")

                // Correlation
                .setHeader(SolaceHeaders.CORRELATION_ID, UUID.randomUUID().toString())

                // DMQ eligibility
                .setHeader(SolaceHeaders.DMQ_ELIGIBLE, true)

                .build();

            solaceTemplate.send("order/created", message);
            """);

        System.out.println("3. Transaction support:");
        System.out.println("""
            @Service
            public class TransactionalService {

                @Autowired
                private JCSMPSession session;

                @Transactional
                public void processWithTransaction(Order order) throws JCSMPException {
                    // Start transaction
                    session.startTransaction();

                    try {
                        // Publish messages
                        publishOrderCreated(order);
                        publishInventoryUpdate(order);

                        // Database operations
                        orderRepository.save(order);

                        // Commit transaction
                        session.commit();

                    } catch (Exception e) {
                        // Rollback both messaging and database
                        session.rollback();
                        throw e;
                    }
                }
            }
            """);

        System.out.println("4. Monitoring and metrics:");
        System.out.println("""
            @Component
            public class SolaceMetrics {

                @Autowired
                private JCSMPSession session;

                public void printStats() {
                    SessionStats stats = session.getSessionStats();

                    System.out.println("Messages sent: " + stats.getTotalMsgsSent());
                    System.out.println("Messages received: " + stats.getTotalMsgsRecv());
                    System.out.println("Bytes sent: " + stats.getTotalBytesSent());
                    System.out.println("Bytes received: " + stats.getTotalBytesRecv());

                    // Queue stats via SEMP (Solace Element Management Protocol)
                    // Requires management API access
                }
            }
            """);

        System.out.println("5. Best practices:");
        System.out.println("   ✓ Use guaranteed messaging for critical data");
        System.out.println("   ✓ Set appropriate TTL for messages");
        System.out.println("   ✓ Configure DMQ for failed messages");
        System.out.println("   ✓ Use hierarchical topic naming (e.g., domain/entity/action)");
        System.out.println("   ✓ Leverage wildcards for flexible subscriptions");
        System.out.println("   ✓ Enable message compression for large payloads");
        System.out.println("   ✓ Monitor queue depth and DMQ");
        System.out.println("   ✓ Use exclusive queues for ordered processing");
        System.out.println("   ✓ Use non-exclusive queues for load balancing");
        System.out.println("   ✗ Don't create too many queues (manage via subscriptions)");
        System.out.println("   ✗ Don't ignore DMQ messages");
        System.out.println("   ✗ Don't use overly broad wildcards");

        System.out.println("\n6. Common patterns:");
        System.out.println("""
            Pattern                  | Use Case
            -------------------------+------------------------------------------
            Pub/Sub (Topic)         | Event notifications, broadcasts
            Queue (Load balance)    | Task distribution, worker pools
            Request-Reply           | Synchronous service calls
            Topic Endpoint          | Durable pub/sub subscriptions
            Priority queuing        | SLA-based message processing
            Filtered subscriptions  | Selective message consumption
            Last-Value Queue        | State updates (only latest matters)
            """);

        System.out.println();
    }
}
