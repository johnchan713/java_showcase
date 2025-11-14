package com.example.demo.showcase;

/**
 * Demonstrates JMS (Java Message Service) with Spring Boot
 * Covers message producers, consumers, queues, topics, and transactions
 *
 * Note: This is a demonstration - requires JMS broker (ActiveMQ/Artemis)
 */
public class JMSShowcase {

    public static void demonstrate() {
        System.out.println("\n========== JMS (JAVA MESSAGE SERVICE) SHOWCASE ==========\n");

        jmsOverviewDemo();
        producerDemo();
        consumerDemo();
        topicsDemo();
        advancedFeaturesDemo();
    }

    // ========== Overview ==========

    private static void jmsOverviewDemo() {
        System.out.println("--- JMS Overview ---");
        System.out.println("Java API for enterprise messaging\n");

        System.out.println("1. Core concepts:");
        System.out.println("   Queue: Point-to-point (one consumer)");
        System.out.println("   Topic: Publish-subscribe (multiple subscribers)");
        System.out.println("   Producer: Sends messages");
        System.out.println("   Consumer: Receives messages");
        System.out.println("   Message: Header + Properties + Body");

        System.out.println("\n2. Message types:");
        System.out.println("   TextMessage: String content");
        System.out.println("   ObjectMessage: Serializable object");
        System.out.println("   BytesMessage: Binary data");
        System.out.println("   MapMessage: Key-value pairs");
        System.out.println("   StreamMessage: Stream of primitives");

        System.out.println("\n3. Dependencies (ActiveMQ Artemis):");
        System.out.println("""
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-artemis</artifactId>
            </dependency>

            # application.properties
            spring.artemis.mode=embedded
            spring.artemis.host=localhost
            spring.artemis.port=61616
            spring.artemis.user=admin
            spring.artemis.password=admin
            """);

        System.out.println();
    }

    // ========== Producer ==========

    private static void producerDemo() {
        System.out.println("--- JMS Producer ---");
        System.out.println("Send messages to queues and topics\n");

        System.out.println("1. JmsTemplate (basic):");
        System.out.println("""
            @Service
            public class MessageProducer {

                @Autowired
                private JmsTemplate jmsTemplate;

                public void sendMessage(String message) {
                    jmsTemplate.convertAndSend("orderQueue", message);
                }

                public void sendWithHeaders(String message) {
                    jmsTemplate.convertAndSend("orderQueue", message, m -> {
                        m.setStringProperty("priority", "high");
                        m.setLongProperty("timestamp", System.currentTimeMillis());
                        return m;
                    });
                }

                public void sendObject(Order order) {
                    jmsTemplate.convertAndSend("orderQueue", order);
                }
            }
            """);

        System.out.println("2. JmsMessagingTemplate (Spring abstraction):");
        System.out.println("""
            @Service
            public class MessageProducer {

                @Autowired
                private JmsMessagingTemplate jmsMessagingTemplate;

                public void sendMessage(String message) {
                    jmsMessagingTemplate.convertAndSend("orderQueue", message);
                }

                public void sendWithHeaders(Order order) {
                    Map<String, Object> headers = new HashMap<>();
                    headers.put("priority", "high");
                    headers.put("userId", "user123");

                    jmsMessagingTemplate.convertAndSend(
                        "orderQueue", order, headers
                    );
                }
            }
            """);

        System.out.println("3. Message priority and expiration:");
        System.out.println("""
            public void sendPriorityMessage(String message) {
                jmsTemplate.convertAndSend("orderQueue", message, m -> {
                    m.setJMSPriority(9);  // 0-9, higher = more priority
                    m.setJMSExpiration(System.currentTimeMillis() + 60000);  // 1 min
                    return m;
                });
            }
            """);

        System.out.println("4. Reply-to pattern:");
        System.out.println("""
            public String sendAndReceive(String request) {
                return jmsTemplate.convertSendAndReceive(
                    "requestQueue",
                    request,
                    String.class
                );
            }
            """);

        System.out.println();
    }

    // ========== Consumer ==========

    private static void consumerDemo() {
        System.out.println("--- JMS Consumer ---");
        System.out.println("Receive messages from queues\n");

        System.out.println("1. @JmsListener:");
        System.out.println("""
            @Component
            public class MessageConsumer {

                @JmsListener(destination = "orderQueue")
                public void receiveMessage(String message) {
                    System.out.println("Received: " + message);
                }

                @JmsListener(destination = "orderQueue")
                public void receiveOrder(Order order) {
                    System.out.println("Processing order: " + order.getId());
                }
            }
            """);

        System.out.println("2. Access message headers:");
        System.out.println("""
            @JmsListener(destination = "orderQueue")
            public void receiveWithHeaders(
                    String message,
                    @Header("priority") String priority,
                    @Header("JMSMessageID") String messageId,
                    @Header("JMSTimestamp") Long timestamp) {

                System.out.println("Message: " + message);
                System.out.println("Priority: " + priority);
                System.out.println("ID: " + messageId);
                System.out.println("Timestamp: " + new Date(timestamp));
            }

            // Or get javax.jms.Message
            @JmsListener(destination = "orderQueue")
            public void receiveMessage(Message message) throws JMSException {
                String text = ((TextMessage) message).getText();
                String priority = message.getStringProperty("priority");
                String correlationId = message.getJMSCorrelationID();
            }
            """);

        System.out.println("3. Concurrent consumers:");
        System.out.println("""
            @JmsListener(
                destination = "orderQueue",
                concurrency = "3-10"  // Min 3, max 10 consumers
            )
            public void receiveMessage(String message) {
                // Process message
            }
            """);

        System.out.println("4. Message selector:");
        System.out.println("""
            @JmsListener(
                destination = "orderQueue",
                selector = "priority = 'high'"  // Only high priority
            )
            public void receiveHighPriority(String message) {
                // Process high priority messages
            }

            @JmsListener(
                destination = "orderQueue",
                selector = "age > 18 AND country = 'US'"
            )
            public void receiveFiltered(String message) {
                // Filtered messages
            }
            """);

        System.out.println("5. Reply pattern:");
        System.out.println("""
            @JmsListener(destination = "requestQueue")
            @SendTo("responseQueue")  // Send result to response queue
            public String processRequest(String request) {
                return "Processed: " + request;
            }

            // Or reply to JMSReplyTo
            @JmsListener(destination = "requestQueue")
            public String handleRequest(String request) {
                return "Response for: " + request;
                // Automatically sent to JMSReplyTo destination
            }
            """);

        System.out.println();
    }

    // ========== Topics (Pub/Sub) ==========

    private static void topicsDemo() {
        System.out.println("--- Topics (Pub/Sub) ---");
        System.out.println("Publish-subscribe messaging\n");

        System.out.println("1. Publish to topic:");
        System.out.println("""
            @Service
            public class EventPublisher {

                @Autowired
                private JmsTemplate jmsTemplate;

                public void publishEvent(String event) {
                    jmsTemplate.setPubSubDomain(true);  // Enable topic mode
                    jmsTemplate.convertAndSend("eventTopic", event);
                }
            }

            // Or configure topic in properties
            spring.jms.pub-sub-domain=true
            """);

        System.out.println("2. Subscribe to topic:");
        System.out.println("""
            @Component
            public class EventSubscriber {

                @JmsListener(destination = "eventTopic", containerFactory = "topicListenerFactory")
                public void handleEvent(String event) {
                    System.out.println("Subscriber 1 received: " + event);
                }
            }

            @Component
            public class AnotherSubscriber {

                @JmsListener(destination = "eventTopic", containerFactory = "topicListenerFactory")
                public void handleEvent(String event) {
                    System.out.println("Subscriber 2 received: " + event);
                }
            }

            // Both subscribers receive the same message
            """);

        System.out.println("3. Configure topic listener:");
        System.out.println("""
            @Configuration
            public class JmsConfig {

                @Bean
                public JmsListenerContainerFactory<?> topicListenerFactory(
                        ConnectionFactory connectionFactory) {

                    DefaultJmsListenerContainerFactory factory =
                        new DefaultJmsListenerContainerFactory();

                    factory.setConnectionFactory(connectionFactory);
                    factory.setPubSubDomain(true);  // Topic mode
                    factory.setConcurrency("1-5");

                    return factory;
                }
            }
            """);

        System.out.println("4. Durable subscriptions:");
        System.out.println("""
            @JmsListener(
                destination = "eventTopic",
                containerFactory = "durableTopicListenerFactory",
                subscription = "myDurableSubscription"
            )
            public void handleDurableEvent(String event) {
                // Receives messages even if temporarily disconnected
            }

            @Bean
            public JmsListenerContainerFactory<?> durableTopicListenerFactory(
                    ConnectionFactory connectionFactory) {

                DefaultJmsListenerContainerFactory factory =
                    new DefaultJmsListenerContainerFactory();

                factory.setConnectionFactory(connectionFactory);
                factory.setPubSubDomain(true);
                factory.setSubscriptionDurable(true);
                factory.setClientId("myClientId");

                return factory;
            }
            """);

        System.out.println();
    }

    // ========== Advanced Features ==========

    private static void advancedFeaturesDemo() {
        System.out.println("--- Advanced JMS Features ---");
        System.out.println("Transactions, error handling, and best practices\n");

        System.out.println("1. Transactions:");
        System.out.println("""
            @Configuration
            public class JmsConfig {

                @Bean
                public JmsListenerContainerFactory<?> transactionalListenerFactory(
                        ConnectionFactory connectionFactory,
                        PlatformTransactionManager transactionManager) {

                    DefaultJmsListenerContainerFactory factory =
                        new DefaultJmsListenerContainerFactory();

                    factory.setConnectionFactory(connectionFactory);
                    factory.setTransactionManager(transactionManager);
                    factory.setSessionTransacted(true);

                    return factory;
                }
            }

            @JmsListener(
                destination = "orderQueue",
                containerFactory = "transactionalListenerFactory"
            )
            @Transactional
            public void processOrder(Order order) {
                // Save to database
                orderRepository.save(order);

                // Send confirmation (both commit or rollback together)
                jmsTemplate.convertAndSend("confirmationQueue", order.getId());

                // If exception thrown, both database and JMS rollback
            }
            """);

        System.out.println("2. Error handling:");
        System.out.println("""
            @Bean
            public ErrorHandler errorHandler() {
                return new ErrorHandler() {
                    @Override
                    public void handleError(Throwable t) {
                        log.error("Error in listener", t);
                    }
                };
            }

            @Bean
            public JmsListenerContainerFactory<?> errorHandlingFactory(
                    ConnectionFactory connectionFactory) {

                DefaultJmsListenerContainerFactory factory =
                    new DefaultJmsListenerContainerFactory();

                factory.setConnectionFactory(connectionFactory);
                factory.setErrorHandler(errorHandler());

                // Redelivery
                factory.setSessionTransacted(true);
                // Message redelivered on exception

                return factory;
            }
            """);

        System.out.println("3. Dead Letter Queue:");
        System.out.println("""
            # Configure in broker (ActiveMQ Artemis)
            # Messages that fail repeatedly go to DLQ

            @JmsListener(destination = "DLQ.orderQueue")
            public void handleDeadLetters(Message message) {
                log.error("Message sent to DLQ: {}", message);
                // Manual investigation, alerting
            }
            """);

        System.out.println("4. Message converters:");
        System.out.println("""
            @Bean
            public MessageConverter jacksonJmsMessageConverter() {
                MappingJackson2MessageConverter converter =
                    new MappingJackson2MessageConverter();

                converter.setTargetType(MessageType.TEXT);
                converter.setTypeIdPropertyName("_type");

                return converter;
            }

            @Bean
            public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
                JmsTemplate template = new JmsTemplate(connectionFactory);
                template.setMessageConverter(jacksonJmsMessageConverter());
                return template;
            }
            """);

        System.out.println("5. Best practices:");
        System.out.println("   ✓ Use transactions for critical operations");
        System.out.println("   ✓ Configure error handling and DLQ");
        System.out.println("   ✓ Set appropriate concurrency");
        System.out.println("   ✓ Use message selectors to filter");
        System.out.println("   ✓ Set expiration for time-sensitive messages");
        System.out.println("   ✓ Monitor queue depth and DLQ");
        System.out.println("   ✓ Use durable subscriptions for critical topics");
        System.out.println("   ✗ Don't process messages synchronously if slow");
        System.out.println("   ✗ Don't ignore DLQ messages");

        System.out.println("\n6. Queue vs Topic:");
        System.out.println("   Queue:");
        System.out.println("     ✓ Point-to-point");
        System.out.println("     ✓ One consumer per message");
        System.out.println("     ✓ Load balancing across consumers");
        System.out.println("     USE: Task distribution, commands");
        System.out.println("\n   Topic:");
        System.out.println("     ✓ Publish-subscribe");
        System.out.println("     ✓ All subscribers receive message");
        System.out.println("     ✓ Broadcast to multiple systems");
        System.out.println("     USE: Events, notifications");

        System.out.println();
    }
}
