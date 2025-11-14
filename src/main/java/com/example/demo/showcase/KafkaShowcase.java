package com.example.demo.showcase;

/**
 * Demonstrates Apache Kafka with Spring Kafka
 * Covers producer/consumer configuration, message publishing/consuming, and best practices
 *
 * Note: This is a demonstration showcase - Kafka server not required to run
 * To actually use Kafka, you need a running Kafka broker
 */
public class KafkaShowcase {

    public static void demonstrate() {
        System.out.println("\n========== KAFKA SHOWCASE ==========\n");

        kafkaOverviewDemo();
        producerConfigurationDemo();
        producerUsageDemo();
        consumerConfigurationDemo();
        consumerUsageDemo();
        serializationDemo();
        errorHandlingDemo();
        advancedFeaturesDemo();
    }

    // ========== Kafka Overview ==========

    private static void kafkaOverviewDemo() {
        System.out.println("--- Kafka Overview ---");
        System.out.println("Distributed event streaming platform\n");

        System.out.println("1. Core Concepts:");
        System.out.println("   Producer: Publishes messages to topics");
        System.out.println("   Consumer: Subscribes to topics and processes messages");
        System.out.println("   Topic: Category/feed name for messages");
        System.out.println("   Partition: Topic is split into ordered partitions");
        System.out.println("   Offset: Unique sequential ID for each message in partition");
        System.out.println("   Consumer Group: Group of consumers sharing workload");
        System.out.println("   Broker: Kafka server that stores and serves messages");

        System.out.println("\n2. Message flow:");
        System.out.println("   Producer → Topic (Partition 0, 1, 2...) → Consumer Group");
        System.out.println("   - Each partition consumed by ONE consumer in group");
        System.out.println("   - Multiple partitions enable parallel processing");
        System.out.println("   - Messages in same partition are ordered");

        System.out.println("\n3. Dependencies:");
        System.out.println("""
            <dependency>
                <groupId>org.springframework.kafka</groupId>
                <artifactId>spring-kafka</artifactId>
            </dependency>
            """);

        System.out.println();
    }

    // ========== Producer Configuration ==========

    private static void producerConfigurationDemo() {
        System.out.println("--- Kafka Producer Configuration ---");
        System.out.println("Configure producer for message publishing\n");

        System.out.println("1. application.properties/yml:");
        System.out.println("""
            # Kafka broker address
            spring.kafka.bootstrap-servers=localhost:9092

            # Producer configuration
            spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
            spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer

            # Acknowledgment settings
            spring.kafka.producer.acks=all          # all: wait for all replicas (safest)
                                                    # 1: wait for leader only (default)
                                                    # 0: no acknowledgment (fastest, least safe)

            # Retry settings
            spring.kafka.producer.retries=3         # Number of retry attempts
            spring.kafka.producer.properties.retry.backoff.ms=1000  # Wait between retries

            # Batching for performance
            spring.kafka.producer.batch-size=16384  # Batch size in bytes
            spring.kafka.producer.properties.linger.ms=10  # Wait time to batch messages

            # Compression
            spring.kafka.producer.compression-type=snappy  # none, gzip, snappy, lz4, zstd

            # Idempotence (exactly-once semantics)
            spring.kafka.producer.properties.enable.idempotence=true
            """);

        System.out.println("2. Java Configuration:");
        System.out.println("""
            @Configuration
            public class KafkaProducerConfig {

                @Value("${spring.kafka.bootstrap-servers}")
                private String bootstrapServers;

                @Bean
                public ProducerFactory<String, String> producerFactory() {
                    Map<String, Object> config = new HashMap<>();

                    // Bootstrap servers
                    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

                    // Serializers
                    config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                              StringSerializer.class);
                    config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                              StringSerializer.class);

                    // Acknowledgment
                    config.put(ProducerConfig.ACKS_CONFIG, "all");

                    // Retries and timeout
                    config.put(ProducerConfig.RETRIES_CONFIG, 3);
                    config.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);

                    // Batching
                    config.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
                    config.put(ProducerConfig.LINGER_MS_CONFIG, 10);

                    // Compression
                    config.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");

                    // Idempotence
                    config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

                    // Max in-flight requests (for ordering guarantee)
                    config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);

                    return new DefaultKafkaProducerFactory<>(config);
                }

                @Bean
                public KafkaTemplate<String, String> kafkaTemplate() {
                    return new KafkaTemplate<>(producerFactory());
                }
            }
            """);

        System.out.println("3. JSON Serialization:");
        System.out.println("""
            @Bean
            public ProducerFactory<String, Object> jsonProducerFactory() {
                Map<String, Object> config = new HashMap<>();
                config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
                config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                          StringSerializer.class);
                config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                          JsonSerializer.class);

                // Trust all packages for JSON serialization
                config.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);

                return new DefaultKafkaProducerFactory<>(config);
            }

            @Bean
            public KafkaTemplate<String, Object> jsonKafkaTemplate() {
                return new KafkaTemplate<>(jsonProducerFactory());
            }
            """);

        System.out.println("4. Key Configuration Parameters:");
        System.out.println("   acks: Durability vs speed tradeoff");
        System.out.println("     all (✓): Wait for all replicas - safest, slowest");
        System.out.println("     1: Wait for leader only - balanced");
        System.out.println("     0: No wait - fastest, can lose data");
        System.out.println("   enable.idempotence: Exactly-once delivery guarantee");
        System.out.println("   batch.size: Larger = better throughput, higher latency");
        System.out.println("   linger.ms: Wait time for batching (0 = send immediately)");
        System.out.println("   compression.type: Reduce network bandwidth");

        System.out.println();
    }

    // ========== Producer Usage ==========

    private static void producerUsageDemo() {
        System.out.println("--- Kafka Producer Usage ---");
        System.out.println("Send messages to Kafka topics\n");

        System.out.println("1. Basic producer:");
        System.out.println("""
            @Service
            public class MessageProducer {

                @Autowired
                private KafkaTemplate<String, String> kafkaTemplate;

                private static final String TOPIC = "user-events";

                public void sendMessage(String message) {
                    // Fire and forget (async, no result)
                    kafkaTemplate.send(TOPIC, message);
                }

                public void sendMessageWithKey(String key, String message) {
                    // Messages with same key go to same partition (ordering)
                    kafkaTemplate.send(TOPIC, key, message);
                }

                public void sendToPartition(int partition, String key, String message) {
                    // Send to specific partition
                    kafkaTemplate.send(TOPIC, partition, key, message);
                }
            }
            """);

        System.out.println("2. Synchronous send (wait for result):");
        System.out.println("""
            public void sendMessageSync(String message) {
                try {
                    // Block until send completes or fails
                    SendResult<String, String> result =
                        kafkaTemplate.send(TOPIC, message).get(10, TimeUnit.SECONDS);

                    RecordMetadata metadata = result.getRecordMetadata();
                    System.out.println("Message sent successfully:");
                    System.out.println("  Topic: " + metadata.topic());
                    System.out.println("  Partition: " + metadata.partition());
                    System.out.println("  Offset: " + metadata.offset());
                    System.out.println("  Timestamp: " + metadata.timestamp());

                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    System.err.println("Failed to send message: " + e.getMessage());
                    // Handle failure (retry, log, alert, etc.)
                }
            }
            """);

        System.out.println("3. Asynchronous send with callback:");
        System.out.println("""
            public void sendMessageAsync(String message) {
                CompletableFuture<SendResult<String, String>> future =
                    kafkaTemplate.send(TOPIC, message);

                // Add callback for success/failure
                future.whenComplete((result, ex) -> {
                    if (ex == null) {
                        RecordMetadata metadata = result.getRecordMetadata();
                        System.out.println("Message sent successfully:");
                        System.out.println("  Partition: " + metadata.partition());
                        System.out.println("  Offset: " + metadata.offset());
                    } else {
                        System.err.println("Failed to send message: " + ex.getMessage());
                        // Handle failure
                    }
                });
            }

            // Alternative: Using ListenableFuture callback
            public void sendWithCallback(String message) {
                ListenableFuture<SendResult<String, String>> future =
                    kafkaTemplate.send(TOPIC, message);

                future.addCallback(
                    result -> {
                        // Success
                        System.out.println("Sent: " + message +
                            " offset: " + result.getRecordMetadata().offset());
                    },
                    ex -> {
                        // Failure
                        System.err.println("Failed: " + ex.getMessage());
                    }
                );
            }
            """);

        System.out.println("4. Send JSON objects:");
        System.out.println("""
            @Autowired
            private KafkaTemplate<String, Object> jsonKafkaTemplate;

            public void sendUserEvent(User user) {
                UserEvent event = new UserEvent(user.getId(), "USER_CREATED",
                    Instant.now());
                jsonKafkaTemplate.send("user-events", event.getUserId(), event);
            }

            @Data
            @AllArgsConstructor
            public class UserEvent {
                private String userId;
                private String eventType;
                private Instant timestamp;
            }
            """);

        System.out.println("5. Producer with headers:");
        System.out.println("""
            public void sendWithHeaders(String key, String message) {
                ProducerRecord<String, String> record =
                    new ProducerRecord<>(TOPIC, key, message);

                // Add custom headers
                record.headers().add("source", "user-service".getBytes());
                record.headers().add("version", "1.0".getBytes());
                record.headers().add("correlation-id", UUID.randomUUID()
                    .toString().getBytes());

                kafkaTemplate.send(record);
            }
            """);

        System.out.println("6. Transactions (exactly-once semantics):");
        System.out.println("""
            @Transactional("kafkaTransactionManager")
            public void sendTransactional(List<String> messages) {
                // All messages sent or none (atomic)
                for (String message : messages) {
                    kafkaTemplate.send(TOPIC, message);
                }
                // If exception thrown, all messages rolled back
            }

            // Configure transaction manager in config
            @Bean
            public KafkaTransactionManager<String, String> kafkaTransactionManager(
                    ProducerFactory<String, String> producerFactory) {
                return new KafkaTransactionManager<>(producerFactory);
            }
            """);

        System.out.println();
    }

    // ========== Consumer Configuration ==========

    private static void consumerConfigurationDemo() {
        System.out.println("--- Kafka Consumer Configuration ---");
        System.out.println("Configure consumer for message consumption\n");

        System.out.println("1. application.properties/yml:");
        System.out.println("""
            # Consumer configuration
            spring.kafka.consumer.bootstrap-servers=localhost:9092
            spring.kafka.consumer.group-id=my-consumer-group
            spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
            spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer

            # Auto-offset management
            spring.kafka.consumer.auto-offset-reset=earliest  # earliest, latest, none
            spring.kafka.consumer.enable-auto-commit=true     # Auto-commit offsets
            spring.kafka.consumer.auto-commit-interval=5000   # Commit interval (ms)

            # Consumer session and heartbeat
            spring.kafka.consumer.properties.session.timeout.ms=30000
            spring.kafka.consumer.properties.heartbeat.interval.ms=3000

            # Fetch settings
            spring.kafka.consumer.max-poll-records=500        # Max records per poll
            spring.kafka.consumer.fetch-min-size=1            # Min bytes to fetch
            spring.kafka.consumer.fetch-max-wait=500          # Max wait time (ms)

            # Listener configuration
            spring.kafka.listener.ack-mode=BATCH              # RECORD, BATCH, TIME, COUNT, etc.
            spring.kafka.listener.concurrency=3               # Number of consumer threads
            """);

        System.out.println("2. Java Configuration:");
        System.out.println("""
            @Configuration
            @EnableKafka
            public class KafkaConsumerConfig {

                @Value("${spring.kafka.bootstrap-servers}")
                private String bootstrapServers;

                @Bean
                public ConsumerFactory<String, String> consumerFactory() {
                    Map<String, Object> config = new HashMap<>();

                    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
                    config.put(ConsumerConfig.GROUP_ID_CONFIG, "my-consumer-group");

                    // Deserializers
                    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                              StringDeserializer.class);
                    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                              StringDeserializer.class);

                    // Auto-offset reset
                    config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

                    // Auto-commit
                    config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
                    config.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 5000);

                    // Session and heartbeat
                    config.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 30000);
                    config.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 3000);

                    // Max poll records
                    config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);

                    return new DefaultKafkaConsumerFactory<>(config);
                }

                @Bean
                public ConcurrentKafkaListenerContainerFactory<String, String>
                        kafkaListenerContainerFactory() {

                    ConcurrentKafkaListenerContainerFactory<String, String> factory =
                        new ConcurrentKafkaListenerContainerFactory<>();

                    factory.setConsumerFactory(consumerFactory());

                    // Concurrent consumers (threads)
                    factory.setConcurrency(3);

                    // Acknowledgment mode
                    factory.getContainerProperties().setAckMode(AckMode.BATCH);

                    // Error handler
                    factory.setCommonErrorHandler(new DefaultErrorHandler(
                        new FixedBackOff(1000L, 3L)  // Retry 3 times with 1s delay
                    ));

                    return factory;
                }
            }
            """);

        System.out.println("3. JSON Deserialization:");
        System.out.println("""
            @Bean
            public ConsumerFactory<String, Object> jsonConsumerFactory() {
                Map<String, Object> config = new HashMap<>();
                config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
                config.put(ConsumerConfig.GROUP_ID_CONFIG, "json-consumer-group");
                config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                          StringDeserializer.class);
                config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                          JsonDeserializer.class);

                // Trust specific packages for security
                config.put(JsonDeserializer.TRUSTED_PACKAGES, "com.example.demo.model");
                // Or trust all (use with caution)
                config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

                return new DefaultKafkaConsumerFactory<>(config);
            }
            """);

        System.out.println("4. Key Configuration Parameters:");
        System.out.println("   group.id: Consumer group identifier");
        System.out.println("   auto.offset.reset: Where to start when no offset");
        System.out.println("     earliest: Start from beginning");
        System.out.println("     latest: Start from newest messages");
        System.out.println("     none: Throw exception if no offset");
        System.out.println("   enable.auto.commit: Automatic vs manual offset commit");
        System.out.println("   max.poll.records: Batch size per poll");
        System.out.println("   session.timeout.ms: Consumer considered dead after timeout");

        System.out.println();
    }

    // ========== Consumer Usage ==========

    private static void consumerUsageDemo() {
        System.out.println("--- Kafka Consumer Usage ---");
        System.out.println("Consume messages from Kafka topics\n");

        System.out.println("1. Basic consumer:");
        System.out.println("""
            @Service
            public class MessageConsumer {

                @KafkaListener(topics = "user-events", groupId = "my-consumer-group")
                public void listen(String message) {
                    System.out.println("Received: " + message);
                    // Process message
                }

                @KafkaListener(topics = {"topic1", "topic2"}, groupId = "multi-topic-group")
                public void listenMultipleTopics(String message) {
                    // Listen to multiple topics
                }

                @KafkaListener(topicPattern = "user-.*", groupId = "pattern-group")
                public void listenPattern(String message) {
                    // Listen to topics matching regex pattern
                }
            }
            """);

        System.out.println("2. Consumer with metadata:");
        System.out.println("""
            @KafkaListener(topics = "user-events", groupId = "metadata-group")
            public void listenWithMetadata(
                    String message,
                    @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                    @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                    @Header(KafkaHeaders.OFFSET) long offset,
                    @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp,
                    @Header(KafkaHeaders.RECEIVED_KEY) String key) {

                System.out.println("Message: " + message);
                System.out.println("Topic: " + topic);
                System.out.println("Partition: " + partition);
                System.out.println("Offset: " + offset);
                System.out.println("Timestamp: " + new Date(timestamp));
                System.out.println("Key: " + key);
            }
            """);

        System.out.println("3. Consumer with ConsumerRecord:");
        System.out.println("""
            @KafkaListener(topics = "user-events", groupId = "record-group")
            public void listenRecord(ConsumerRecord<String, String> record) {
                System.out.println("Topic: " + record.topic());
                System.out.println("Partition: " + record.partition());
                System.out.println("Offset: " + record.offset());
                System.out.println("Key: " + record.key());
                System.out.println("Value: " + record.value());
                System.out.println("Timestamp: " + record.timestamp());

                // Access headers
                Headers headers = record.headers();
                headers.forEach(header -> {
                    System.out.println("Header: " + header.key() +
                        " = " + new String(header.value()));
                });
            }
            """);

        System.out.println("4. Batch consumption:");
        System.out.println("""
            @KafkaListener(topics = "user-events", groupId = "batch-group")
            public void listenBatch(List<String> messages) {
                System.out.println("Received batch of " + messages.size() + " messages");
                for (String message : messages) {
                    // Process batch
                }
            }

            @KafkaListener(topics = "user-events", groupId = "batch-record-group")
            public void listenBatchRecords(List<ConsumerRecord<String, String>> records) {
                System.out.println("Processing batch of " + records.size() + " records");
                for (ConsumerRecord<String, String> record : records) {
                    // Process with metadata
                }
            }
            """);

        System.out.println("5. Manual acknowledgment:");
        System.out.println("""
            // In configuration: factory.getContainerProperties().setAckMode(AckMode.MANUAL);

            @KafkaListener(topics = "user-events", groupId = "manual-ack-group")
            public void listenManualAck(String message, Acknowledgment ack) {
                try {
                    // Process message
                    System.out.println("Processing: " + message);

                    // Manually commit offset after successful processing
                    ack.acknowledge();

                } catch (Exception e) {
                    // Don't acknowledge - message will be reprocessed
                    System.err.println("Error processing message: " + e.getMessage());
                }
            }

            @KafkaListener(topics = "user-events", groupId = "manual-batch-ack-group")
            public void listenBatchManualAck(List<String> messages, Acknowledgment ack) {
                try {
                    // Process all messages
                    messages.forEach(this::process);

                    // Acknowledge entire batch
                    ack.acknowledge();

                } catch (Exception e) {
                    // Batch will be reprocessed
                }
            }
            """);

        System.out.println("6. Consume JSON objects:");
        System.out.println("""
            @KafkaListener(topics = "user-events",
                          groupId = "json-group",
                          containerFactory = "jsonKafkaListenerContainerFactory")
            public void listenUserEvent(UserEvent event) {
                System.out.println("User event: " + event.getEventType());
                System.out.println("User ID: " + event.getUserId());
                System.out.println("Timestamp: " + event.getTimestamp());
            }
            """);

        System.out.println("7. Partition assignment:");
        System.out.println("""
            @KafkaListener(
                topics = "user-events",
                groupId = "partition-group",
                topicPartitions = @TopicPartition(
                    topic = "user-events",
                    partitions = {"0", "1"}  // Listen to specific partitions only
                )
            )
            public void listenSpecificPartitions(String message) {
                // Only receives messages from partitions 0 and 1
            }

            @KafkaListener(
                topics = "user-events",
                groupId = "partition-range-group",
                topicPartitions = @TopicPartition(
                    topic = "user-events",
                    partitionOffsets = {
                        @PartitionOffset(partition = "0", initialOffset = "100"),
                        @PartitionOffset(partition = "1", initialOffset = "200")
                    }
                )
            )
            public void listenFromOffset(String message) {
                // Start from specific offsets
            }
            """);

        System.out.println();
    }

    // ========== Serialization ==========

    private static void serializationDemo() {
        System.out.println("--- Serialization/Deserialization ---");
        System.out.println("Convert objects to/from bytes for Kafka\n");

        System.out.println("1. Built-in serializers:");
        System.out.println("   StringSerializer/Deserializer - String messages");
        System.out.println("   IntegerSerializer/Deserializer - Integer messages");
        System.out.println("   LongSerializer/Deserializer - Long messages");
        System.out.println("   ByteArraySerializer/Deserializer - Raw bytes");
        System.out.println("   JsonSerializer/Deserializer - JSON objects");

        System.out.println("\n2. JSON serialization configuration:");
        System.out.println("""
            # Producer
            spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
            spring.kafka.producer.properties.spring.json.add.type.headers=false

            # Consumer
            spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
            spring.kafka.consumer.properties.spring.json.trusted.packages=*
            """);

        System.out.println("3. Avro serialization (schema registry):");
        System.out.println("""
            <dependency>
                <groupId>io.confluent</groupId>
                <artifactId>kafka-avro-serializer</artifactId>
            </dependency>

            # Configuration
            spring.kafka.producer.value-serializer=io.confluent.kafka.serializers.KafkaAvroSerializer
            spring.kafka.consumer.value-deserializer=io.confluent.kafka.serializers.KafkaAvroDeserializer
            spring.kafka.properties.schema.registry.url=http://localhost:8081
            """);

        System.out.println("4. Custom serializer:");
        System.out.println("""
            public class UserSerializer implements Serializer<User> {

                private final ObjectMapper objectMapper = new ObjectMapper();

                @Override
                public byte[] serialize(String topic, User user) {
                    try {
                        return objectMapper.writeValueAsBytes(user);
                    } catch (JsonProcessingException e) {
                        throw new SerializationException("Error serializing User", e);
                    }
                }
            }

            public class UserDeserializer implements Deserializer<User> {

                private final ObjectMapper objectMapper = new ObjectMapper();

                @Override
                public User deserialize(String topic, byte[] data) {
                    try {
                        return objectMapper.readValue(data, User.class);
                    } catch (IOException e) {
                        throw new SerializationException("Error deserializing User", e);
                    }
                }
            }
            """);

        System.out.println();
    }

    // ========== Error Handling ==========

    private static void errorHandlingDemo() {
        System.out.println("--- Error Handling ---");
        System.out.println("Handle failures in producer and consumer\n");

        System.out.println("1. Producer error handling:");
        System.out.println("""
            public void sendWithErrorHandling(String message) {
                kafkaTemplate.send(TOPIC, message)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            // Log error
                            logger.error("Failed to send message: {}", message, ex);

                            // Specific error handling
                            if (ex instanceof TimeoutException) {
                                // Kafka broker unreachable
                                alertOps("Kafka timeout");
                            } else if (ex instanceof RecordTooLargeException) {
                                // Message too large
                                logger.warn("Message too large, consider compression");
                            }

                            // Retry logic
                            retryMessage(message);
                        }
                    });
            }
            """);

        System.out.println("2. Consumer error handler:");
        System.out.println("""
            @Bean
            public ConcurrentKafkaListenerContainerFactory<String, String>
                    kafkaListenerContainerFactory() {

                ConcurrentKafkaListenerContainerFactory<String, String> factory =
                    new ConcurrentKafkaListenerContainerFactory<>();

                factory.setConsumerFactory(consumerFactory());

                // Error handler with retry
                DefaultErrorHandler errorHandler = new DefaultErrorHandler(
                    new FixedBackOff(1000L, 3L)  // Retry 3 times, 1s delay
                );

                // Don't retry for specific exceptions
                errorHandler.addNotRetryableExceptions(
                    IllegalArgumentException.class,
                    NullPointerException.class
                );

                factory.setCommonErrorHandler(errorHandler);

                return factory;
            }
            """);

        System.out.println("3. Dead Letter Topic (DLT):");
        System.out.println("""
            @Bean
            public ConcurrentKafkaListenerContainerFactory<String, String>
                    kafkaListenerContainerFactory() {

                ConcurrentKafkaListenerContainerFactory<String, String> factory =
                    new ConcurrentKafkaListenerContainerFactory<>();
                factory.setConsumerFactory(consumerFactory());

                // Send failed messages to DLT after retries exhausted
                DeadLetterPublishingRecoverer recoverer =
                    new DeadLetterPublishingRecoverer(kafkaTemplate,
                        (record, ex) -> new TopicPartition(
                            record.topic() + ".DLT",  // Dead letter topic name
                            record.partition()
                        )
                    );

                DefaultErrorHandler errorHandler = new DefaultErrorHandler(
                    recoverer,
                    new FixedBackOff(1000L, 3L)
                );

                factory.setCommonErrorHandler(errorHandler);

                return factory;
            }

            // Consumer for DLT
            @KafkaListener(topics = "user-events.DLT", groupId = "dlt-handler")
            public void handleDeadLetter(ConsumerRecord<String, String> record) {
                logger.error("Message sent to DLT: {}", record.value());
                // Manual investigation, alerting, etc.
            }
            """);

        System.out.println("4. Retry topics (Spring Kafka 2.7+):");
        System.out.println("""
            @Bean
            public RetryTopicConfiguration retryTopicConfiguration(
                    KafkaTemplate<String, String> kafkaTemplate) {

                return RetryTopicConfigurationBuilder
                    .newInstance()
                    .maxAttempts(4)  // Original + 3 retries
                    .fixedBackOff(3000)  // 3 second delay
                    .retryTopicSuffix("-retry")
                    .dltSuffix("-dlt")
                    .dltHandlerMethod("handleDlt")
                    .create(kafkaTemplate);
            }

            @KafkaListener(topics = "user-events")
            public void listen(String message) {
                // May throw exception and trigger retry
                processMessage(message);
            }

            @DltHandler
            public void handleDlt(String message) {
                logger.error("Message sent to DLT after retries: {}", message);
            }
            """);

        System.out.println();
    }

    // ========== Advanced Features ==========

    private static void advancedFeaturesDemo() {
        System.out.println("--- Advanced Kafka Features ---");
        System.out.println("Transactions, exactly-once, and performance tuning\n");

        System.out.println("1. Exactly-once semantics (EOS):");
        System.out.println("""
            # Producer configuration
            spring.kafka.producer.properties.enable.idempotence=true
            spring.kafka.producer.transaction-id-prefix=tx-

            # Consumer configuration
            spring.kafka.consumer.isolation-level=read_committed

            @Configuration
            public class KafkaConfig {

                @Bean
                public KafkaTransactionManager<String, String> kafkaTransactionManager(
                        ProducerFactory<String, String> producerFactory) {
                    return new KafkaTransactionManager<>(producerFactory);
                }
            }

            @Service
            public class TransactionalService {

                @Transactional("kafkaTransactionManager")
                public void processWithTransaction(String input) {
                    // Read from Kafka
                    String result = process(input);

                    // Write to Kafka
                    kafkaTemplate.send("output-topic", result);

                    // All or nothing - atomic operation
                }
            }
            """);

        System.out.println("2. Consumer group rebalancing:");
        System.out.println("""
            @KafkaListener(topics = "user-events", groupId = "rebalance-group")
            public void listen(String message,
                              @Header(KafkaHeaders.GROUP_ID) String groupId) {
                // When new consumer joins/leaves, partitions are rebalanced
                // Each partition assigned to exactly one consumer in group
            }

            // Rebalance listener
            @Bean
            public ConsumerFactory<String, String> consumerFactory() {
                factory.getContainerProperties().setConsumerRebalanceListener(
                    new ConsumerAwareRebalanceListener() {
                        @Override
                        public void onPartitionsAssigned(Consumer<?, ?> consumer,
                                                        Collection<TopicPartition> partitions) {
                            System.out.println("Partitions assigned: " + partitions);
                        }

                        @Override
                        public void onPartitionsRevoked(Consumer<?, ?> consumer,
                                                       Collection<TopicPartition> partitions) {
                            System.out.println("Partitions revoked: " + partitions);
                        }
                    }
                );
            }
            """);

        System.out.println("3. Message filtering:");
        System.out.println("""
            @Bean
            public ConcurrentKafkaListenerContainerFactory<String, String>
                    filteringListenerContainerFactory() {

                ConcurrentKafkaListenerContainerFactory<String, String> factory =
                    new ConcurrentKafkaListenerContainerFactory<>();

                factory.setConsumerFactory(consumerFactory());

                // Filter messages before processing
                factory.setRecordFilterStrategy(record -> {
                    // Return true to filter out (skip) message
                    return record.value().contains("SKIP");
                });

                return factory;
            }
            """);

        System.out.println("4. Performance tuning:");
        System.out.println("   Producer:");
        System.out.println("     ✓ Increase batch.size for higher throughput");
        System.out.println("     ✓ Set linger.ms > 0 to batch messages");
        System.out.println("     ✓ Use compression (snappy, lz4)");
        System.out.println("     ✓ Set acks=1 if some data loss acceptable");
        System.out.println("     ✓ Increase buffer.memory for high volume");

        System.out.println("\n   Consumer:");
        System.out.println("     ✓ Increase max.poll.records for batch processing");
        System.out.println("     ✓ Use manual commit for better control");
        System.out.println("     ✓ Increase concurrency (parallel consumers)");
        System.out.println("     ✓ Increase fetch.min.bytes to reduce polls");
        System.out.println("     ✓ Use multiple partitions for parallelism");

        System.out.println("\n5. Monitoring and metrics:");
        System.out.println("""
            # Actuator endpoints
            management.endpoints.web.exposure.include=health,metrics,prometheus
            management.metrics.export.prometheus.enabled=true

            # Key metrics to monitor:
            - kafka.producer.record-send-rate
            - kafka.producer.request-latency-avg
            - kafka.producer.record-error-rate
            - kafka.consumer.fetch-rate
            - kafka.consumer.records-lag-max
            - kafka.consumer.records-consumed-rate
            """);

        System.out.println("\n6. Best practices:");
        System.out.println("   ✓ Use consumer groups for scalability");
        System.out.println("   ✓ One partition per consumer for max parallelism");
        System.out.println("   ✓ Use keys for message ordering within partition");
        System.out.println("   ✓ Monitor consumer lag");
        System.out.println("   ✓ Use manual commit for critical data");
        System.out.println("   ✓ Implement idempotent consumers (handle duplicates)");
        System.out.println("   ✓ Set appropriate retention policy");
        System.out.println("   ✓ Use schema registry for evolution");
        System.out.println("   ✓ Enable compression for large messages");
        System.out.println("   ✗ Don't create too many partitions (overhead)");
        System.out.println("   ✗ Don't use Kafka as a database");

        System.out.println();
    }
}
