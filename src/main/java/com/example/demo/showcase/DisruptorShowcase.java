package com.example.demo.showcase;

/**
 * Demonstrates LMAX Disruptor - High-performance inter-thread messaging
 * Covers Ring Buffer, Event Handlers, and concurrent patterns
 */
public class DisruptorShowcase {

    public static void demonstrate() {
        System.out.println("\n========== LMAX DISRUPTOR SHOWCASE ==========\n");

        System.out.println("--- LMAX Disruptor Overview ---");
        System.out.println("Ultra-low latency inter-thread messaging\n");

        System.out.println("Key concepts:");
        System.out.println("   • Ring Buffer: Pre-allocated circular array");
        System.out.println("   • Event: Data structure for passing information");
        System.out.println("   • Event Handler: Processes events");
        System.out.println("   • Producer: Publishes events");
        System.out.println("   • Wait Strategy: How consumers wait for events");

        System.out.println("\nDependencies:");
        System.out.println("""
            <dependency>
                <groupId>com.lmax</groupId>
                <artifactId>disruptor</artifactId>
                <version>4.0.0</version>
            </dependency>
            """);

        System.out.println("\n--- Basic Setup ---");
        System.out.println("""
            import com.lmax.disruptor.*;
            import com.lmax.disruptor.dsl.Disruptor;
            import com.lmax.disruptor.dsl.ProducerType;
            
            // Event class
            public class OrderEvent {
                private long orderId;
                private String product;
                private int quantity;
                
                // Getters, setters
            }
            
            // Event Factory
            public class OrderEventFactory implements EventFactory<OrderEvent> {
                @Override
                public OrderEvent newInstance() {
                    return new OrderEvent();
                }
            }
            
            // Event Handler
            public class OrderEventHandler implements EventHandler<OrderEvent> {
                @Override
                public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) {
                    System.out.println("Processing order: " + event.getOrderId());
                    // Process event
                }
            }
            """);

        System.out.println("\n--- Disruptor Configuration ---");
        System.out.println("""
            @Configuration
            public class DisruptorConfig {
            
                @Bean
                public Disruptor<OrderEvent> orderDisruptor() {
                    ThreadFactory threadFactory = new ThreadFactory() {
                        private int counter = 0;
                        
                        @Override
                        public Thread newThread(Runnable r) {
                            return new Thread(r, "disruptor-thread-" + counter++);
                        }
                    };
                    
                    int bufferSize = 1024; // Must be power of 2
                    
                    Disruptor<OrderEvent> disruptor = new Disruptor<>(
                        new OrderEventFactory(),
                        bufferSize,
                        threadFactory,
                        ProducerType.MULTI,  // or SINGLE
                        new BlockingWaitStrategy()  // or BusySpinWaitStrategy
                    );
                    
                    // Register event handlers
                    disruptor.handleEventsWith(new OrderEventHandler());
                    
                    // Start disruptor
                    disruptor.start();
                    
                    return disruptor;
                }
            }
            """);

        System.out.println("\n--- Publishing Events ---");
        System.out.println("""
            @Service
            public class OrderProducer {
            
                private final Disruptor<OrderEvent> disruptor;
                
                public OrderProducer(Disruptor<OrderEvent> disruptor) {
                    this.disruptor = disruptor;
                }
                
                public void publishOrder(long orderId, String product, int quantity) {
                    RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();
                    
                    // Get next sequence
                    long sequence = ringBuffer.next();
                    
                    try {
                        // Get event at sequence
                        OrderEvent event = ringBuffer.get(sequence);
                        
                        // Populate event
                        event.setOrderId(orderId);
                        event.setProduct(product);
                        event.setQuantity(quantity);
                        
                    } finally {
                        // Publish
                        ringBuffer.publish(sequence);
                    }
                }
                
                // Using translator
                public void publishWithTranslator(long orderId, String product, int quantity) {
                    RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();
                    
                    EventTranslatorThreeArg<OrderEvent, Long, String, Integer> translator =
                        (event, sequence, id, prod, qty) -> {
                            event.setOrderId(id);
                            event.setProduct(prod);
                            event.setQuantity(qty);
                        };
                    
                    ringBuffer.publishEvent(translator, orderId, product, quantity);
                }
            }
            """);

        System.out.println("\n--- Multiple Event Handlers ---");
        System.out.println("""
            // Sequential processing
            disruptor.handleEventsWith(new ValidationHandler())
                     .then(new PersistenceHandler())
                     .then(new NotificationHandler());
            
            // Parallel processing
            disruptor.handleEventsWith(
                new LoggingHandler(),
                new MetricsHandler(),
                new AuditHandler()
            );
            
            // Diamond pattern
            disruptor.handleEventsWith(new Handler1(), new Handler2())
                     .then(new CombiningHandler());
            """);

        System.out.println("\n--- Wait Strategies ---");
        System.out.println("""
            // BlockingWaitStrategy - CPU friendly, higher latency
            new BlockingWaitStrategy()
            
            // BusySpinWaitStrategy - Lowest latency, high CPU
            new BusySpinWaitStrategy()
            
            // YieldingWaitStrategy - Balanced
            new YieldingWaitStrategy()
            
            // SleepingWaitStrategy - CPU friendly
            new SleepingWaitStrategy()
            
            // TimeoutBlockingWaitStrategy
            new TimeoutBlockingWaitStrategy(1, TimeUnit.SECONDS)
            """);

        System.out.println("\n--- Best Practices ---");
        System.out.println("   ✓ Use power-of-2 buffer size");
        System.out.println("   ✓ Pre-allocate objects in event");
        System.out.println("   ✓ Choose appropriate wait strategy");
        System.out.println("   ✓ Shutdown disruptor properly");
        System.out.println("   ✓ Use event translators for publishing");
        System.out.println("   ✗ Don't use small buffer sizes");
        System.out.println("   ✗ Don't create objects in handlers");
        System.out.println();

        System.out.println("Typical throughput: 6M+ events/second");
        System.out.println("Latency: Sub-microsecond");
        System.out.println();
    }
}
