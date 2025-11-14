package com.example.demo.showcase;

/**
 * Demonstrates RabbitMQ - Message broker with AMQP protocol
 * Covers exchanges, queues, bindings, and messaging patterns
 */
public class RabbitMQShowcase {

    public static void demonstrate() {
        System.out.println("\n========== RABBITMQ SHOWCASE ==========\n");

        System.out.println("--- RabbitMQ Overview ---");
        System.out.println("Advanced Message Queuing Protocol (AMQP) broker\n");

        System.out.println("Key concepts:");
        System.out.println("   • Exchange: Routes messages to queues");
        System.out.println("   • Queue: Stores messages");
        System.out.println("   • Binding: Links exchange to queue");
        System.out.println("   • Routing Key: Used for message routing");
        System.out.println("   • Exchange Types: Direct, Topic, Fanout, Headers");

        System.out.println("\nDependencies:");
        System.out.println("""
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-amqp</artifactId>
            </dependency>
            
            # application.properties
            spring.rabbitmq.host=localhost
            spring.rabbitmq.port=5672
            spring.rabbitmq.username=guest
            spring.rabbitmq.password=guest
            """);

        System.out.println("\n--- Configuration ---");
        System.out.println("""
            @Configuration
            public class RabbitMQConfig {
            
                // Direct Exchange
                @Bean
                public DirectExchange directExchange() {
                    return new DirectExchange("direct.exchange");
                }
                
                @Bean
                public Queue directQueue() {
                    return new Queue("direct.queue", true); // durable
                }
                
                @Bean
                public Binding directBinding() {
                    return BindingBuilder
                        .bind(directQueue())
                        .to(directExchange())
                        .with("direct.routing.key");
                }
                
                // Topic Exchange
                @Bean
                public TopicExchange topicExchange() {
                    return new TopicExchange("topic.exchange");
                }
                
                @Bean
                public Binding topicBinding() {
                    return BindingBuilder
                        .bind(new Queue("topic.queue"))
                        .to(topicExchange())
                        .with("order.#"); // Wildcard pattern
                }
                
                // Fanout Exchange (broadcast)
                @Bean
                public FanoutExchange fanoutExchange() {
                    return new FanoutExchange("fanout.exchange");
                }
            }
            """);

        System.out.println("\n--- Producer (Publisher) ---");
        System.out.println("""
            @Service
            public class MessageProducer {
            
                @Autowired
                private RabbitTemplate rabbitTemplate;
                
                // Send to queue directly
                public void sendToQueue(String message) {
                    rabbitTemplate.convertAndSend("direct.queue", message);
                }
                
                // Send via exchange
                public void sendViaExchange(String message) {
                    rabbitTemplate.convertAndSend(
                        "direct.exchange",
                        "direct.routing.key",
                        message
                    );
                }
                
                // Send object
                public void sendOrder(Order order) {
                    rabbitTemplate.convertAndSend(
                        "topic.exchange",
                        "order.created",
                        order
                    );
                }
                
                // Send with properties
                public void sendWithProperties(String message) {
                    rabbitTemplate.convertAndSend(
                        "direct.exchange",
                        "direct.routing.key",
                        message,
                        msg -> {
                            msg.getMessageProperties().setPriority(5);
                            msg.getMessageProperties().setExpiration("60000");
                            msg.getMessageProperties().setHeader("custom", "value");
                            return msg;
                        }
                    );
                }
            }
            """);

        System.out.println("\n--- Consumer (Listener) ---");
        System.out.println("""
            @Component
            public class MessageConsumer {
            
                @RabbitListener(queues = "direct.queue")
                public void receiveMessage(String message) {
                    System.out.println("Received: " + message);
                }
                
                @RabbitListener(queues = "topic.queue")
                public void receiveOrder(Order order) {
                    System.out.println("Order received: " + order.getId());
                }
                
                // With headers
                @RabbitListener(queues = "direct.queue")
                public void receiveWithHeaders(
                        String message,
                        @Header("custom") String customHeader) {
                    System.out.println("Message: " + message);
                    System.out.println("Header: " + customHeader);
                }
                
                // Manual acknowledgment
                @RabbitListener(queues = "direct.queue", ackMode = "MANUAL")
                public void receiveManualAck(String message, Channel channel,
                        @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
                    try {
                        processMessage(message);
                        channel.basicAck(tag, false); // Acknowledge
                    } catch (Exception e) {
                        channel.basicNack(tag, false, true); // Reject and requeue
                    }
                }
            }
            """);

        System.out.println("\n--- Exchange Types ---");
        System.out.println("""
            1. Direct Exchange:
               - Exact routing key match
               - Use case: Task distribution
               
            2. Topic Exchange:
               - Wildcard routing (* = one word, # = zero or more words)
               - Patterns: "order.created", "order.*.processed", "log.#"
               - Use case: Pub/sub with filtering
               
            3. Fanout Exchange:
               - Broadcasts to all bound queues (ignores routing key)
               - Use case: Notifications to all services
               
            4. Headers Exchange:
               - Routes based on message headers
               - Use case: Complex routing logic
            """);

        System.out.println("\n--- Dead Letter Queue (DLQ) ---");
        System.out.println("""
            @Bean
            public Queue mainQueue() {
                return QueueBuilder.durable("main.queue")
                    .deadLetterExchange("dlx.exchange")
                    .deadLetterRoutingKey("dlq")
                    .ttl(60000) // Message TTL
                    .maxLength(10000)
                    .build();
            }
            
            @Bean
            public Queue deadLetterQueue() {
                return new Queue("dead.letter.queue", true);
            }
            
            @RabbitListener(queues = "dead.letter.queue")
            public void handleDeadLetters(Message message) {
                System.err.println("Dead letter: " + new String(message.getBody()));
                // Log, alert, manual intervention
            }
            """);

        System.out.println("\nBest practices:");
        System.out.println("   ✓ Use durable queues for persistence");
        System.out.println("   ✓ Configure DLQ for failed messages");
        System.out.println("   ✓ Set message TTL appropriately");
        System.out.println("   ✓ Use manual ack for critical messages");
        System.out.println("   ✓ Monitor queue depth");
        System.out.println("   ✗ Don't ignore DLQ messages");
        System.out.println();
    }
}
