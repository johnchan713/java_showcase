package com.example.demo.showcase;

/**
 * Demonstrates Awaitility - Async testing library
 * Covers waiting for conditions, polling, and async assertions
 */
public class AwaitilityShowcase {

    public static void demonstrate() {
        System.out.println("\n========== AWAITILITY SHOWCASE ==========\n");

        System.out.println("--- Awaitility Overview ---");
        System.out.println("DSL for testing asynchronous systems\n");

        System.out.println("Dependencies:");
        System.out.println("""
            <dependency>
                <groupId>org.awaitility</groupId>
                <artifactId>awaitility</artifactId>
                <version>4.2.0</version>
                <scope>test</scope>
            </dependency>
            """);

        System.out.println("\n--- Basic Usage ---");
        System.out.println("""
            import static org.awaitility.Awaitility.*;
            import static java.util.concurrent.TimeUnit.*;
            
            @Test
            public void testAsyncOperation() {
                // Trigger async operation
                service.processAsync();
                
                // Wait until condition is true
                await().atMost(5, SECONDS)
                    .until(() -> service.isComplete());
                
                // Or with message
                await("Processing to complete")
                    .atMost(Duration.ofSeconds(5))
                    .until(service::isComplete);
            }
            """);

        System.out.println("\n--- Waiting for Field Values ---");
        System.out.println("""
            @Test
            public void testFieldValue() {
                Order order = new Order();
                service.processOrder(order);
                
                // Wait for field to have value
                await().atMost(5, SECONDS)
                    .until(() -> order.getStatus(), equalTo(Status.COMPLETED));
                
                // Wait for field not null
                await().until(() -> order.getConfirmationNumber(), notNullValue());
                
                // Wait for collection size
                await().until(() -> order.getItems().size(), greaterThan(0));
            }
            """);

        System.out.println("\n--- Polling Configuration ---");
        System.out.println("""
            // Custom polling interval
            await().pollInterval(100, MILLISECONDS)
                .atMost(5, SECONDS)
                .until(service::isReady);
            
            // Exponential backoff
            await().pollDelay(Duration.ofMillis(100))
                .pollInterval(Duration.ofMillis(200))
                .atMost(Duration.ofSeconds(10))
                .until(service::isReady);
            
            // Fibonacci polling
            await().pollInSameThread()
                .pollInterval(fibonacci())
                .until(service::isReady);
            """);

        System.out.println("\n--- Ignore Exceptions ---");
        System.out.println("""
            // Ignore specific exceptions during polling
            await().ignoreExceptions()
                .atMost(5, SECONDS)
                .until(() -> service.getStatus());
            
            // Ignore specific exception types
            await().ignoreExceptionsMatching(e -> e instanceof NotFoundException)
                .until(() -> repository.findById(id));
            """);

        System.out.println("\n--- Callable and Runnable ---");
        System.out.println("""
            // Wait for Callable
            String result = await().atMost(5, SECONDS)
                .until(() -> service.getResult(), notNullValue());
            
            // Wait for void method
            await().atMost(5, SECONDS)
                .untilAsserted(() -> {
                    assertThat(service.getCount()).isGreaterThan(10);
                    assertThat(service.isActive()).isTrue();
                });
            """);

        System.out.println("\n--- Real-World Examples ---");
        System.out.println("""
            @Test
            public void testMessageProcessing() {
                // Send message
                kafkaTemplate.send("topic", message);
                
                // Wait for message to be consumed
                await().atMost(10, SECONDS)
                    .untilAsserted(() -> {
                        verify(messageHandler, times(1)).handle(any());
                    });
            }
            
            @Test
            public void testDatabaseWrite() {
                service.saveUserAsync(user);
                
                // Wait for database to be updated
                await().atMost(3, SECONDS)
                    .until(() -> userRepository.findById(user.getId()).isPresent());
            }
            
            @Test
            public void testCacheEviction() {
                cacheService.evict("key");
                
                // Wait for cache to be cleared
                await().pollDelay(100, MILLISECONDS)
                    .atMost(2, SECONDS)
                    .until(() -> cacheService.get("key"), nullValue());
            }
            """);

        System.out.println("\n--- Configuration Defaults ---");
        System.out.println("""
            // Set default timeout
            Awaitility.setDefaultTimeout(10, SECONDS);
            Awaitility.setDefaultPollInterval(100, MILLISECONDS);
            
            // Reset to defaults
            Awaitility.reset();
            
            // One-time configuration
            with().pollInterval(50, MILLISECONDS)
                .and().atMost(5, SECONDS)
                .await().until(service::isReady);
            """);

        System.out.println("\nBest practices:");
        System.out.println("   ✓ Use for testing async operations");
        System.out.println("   ✓ Set appropriate timeouts");
        System.out.println("   ✓ Configure polling intervals");
        System.out.println("   ✓ Use descriptive condition messages");
        System.out.println("   ✗ Don't use in production code");
        System.out.println("   ✗ Don't set very long timeouts");
        System.out.println();
    }
}
