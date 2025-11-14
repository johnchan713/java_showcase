package com.example.demo.showcase;

/**
 * Demonstrates Project Reactor - Reactive programming library
 * Covers Mono, Flux, operators, backpressure, and reactive streams
 */
public class ReactorShowcase {

    public static void demonstrate() {
        System.out.println("\n========== PROJECT REACTOR SHOWCASE ==========\n");

        System.out.println("--- Project Reactor Overview ---");
        System.out.println("Reactive programming with Mono and Flux\n");

        System.out.println("Dependencies:");
        System.out.println("""
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-webflux</artifactId>
            </dependency>
            
            Mono<T>: 0 or 1 element
            Flux<T>: 0 to N elements
            """);

        System.out.println("\n--- Creating Publishers ---");
        System.out.println("""
            import reactor.core.publisher.Mono;
            import reactor.core.publisher.Flux;
            
            // Mono - single value
            Mono<String> mono = Mono.just("Hello");
            Mono<String> empty = Mono.empty();
            Mono<String> error = Mono.error(new RuntimeException("Error"));
            
            // Flux - multiple values
            Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5);
            Flux<Integer> range = Flux.range(1, 10);
            Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
            Flux<String> fromList = Flux.fromIterable(List.of("a", "b", "c"));
            """);

        System.out.println("\n--- Subscribing ---");
        System.out.println("""
            // Subscribe
            mono.subscribe(
                value -> System.out.println("Value: " + value),
                error -> System.err.println("Error: " + error),
                () -> System.out.println("Completed")
            );
            
            // Block (for testing only)
            String result = mono.block();
            List<Integer> list = flux.collectList().block();
            """);

        System.out.println("\n--- Transformation Operators ---");
        System.out.println("""
            // map - transform each element
            Flux<Integer> doubled = Flux.range(1, 5)
                .map(n -> n * 2);
            
            // flatMap - async transformation
            Flux<User> users = Flux.just("1", "2", "3")
                .flatMap(id -> userService.findById(id));
            
            // filter - keep only matching elements
            Flux<Integer> evens = Flux.range(1, 10)
                .filter(n -> n % 2 == 0);
            
            // take - limit elements
            Flux<Integer> first3 = flux.take(3);
            
            // skip - skip elements
            Flux<Integer> skipFirst2 = flux.skip(2);
            
            // distinct - remove duplicates
            Flux<String> unique = Flux.just("a", "b", "a", "c").distinct();
            """);

        System.out.println("\n--- Combining Publishers ---");
        System.out.println("""
            Flux<Integer> flux1 = Flux.just(1, 2, 3);
            Flux<Integer> flux2 = Flux.just(4, 5, 6);
            
            // concat - sequential
            Flux<Integer> concatenated = Flux.concat(flux1, flux2);
            
            // merge - interleaved
            Flux<Integer> merged = Flux.merge(flux1, flux2);
            
            // zip - combine pairwise
            Flux<String> zipped = Flux.zip(flux1, flux2,
                (a, b) -> a + "-" + b);
            
            // combineLatest
            Flux.combineLatest(flux1, flux2, (a, b) -> a + b);
            """);

        System.out.println("\n--- Error Handling ---");
        System.out.println("""
            // onErrorReturn - fallback value
            Mono<String> withFallback = mono
                .onErrorReturn("Default");
            
            // onErrorResume - fallback publisher
            Mono<User> withRecovery = userService.findById(id)
                .onErrorResume(error -> userService.getDefaultUser());
            
            // retry
            Mono<String> withRetry = mono
                .retry(3);
            
            // retryWhen
            mono.retryWhen(Retry.backoff(3, Duration.ofSeconds(1)));
            
            // doOnError
            mono.doOnError(error -> log.error("Error occurred", error));
            """);

        System.out.println("\n--- Backpressure ---");
        System.out.println("""
            // onBackpressureBuffer - buffer elements
            Flux<Integer> buffered = flux
                .onBackpressureBuffer(100);
            
            // onBackpressureDrop - drop excess elements
            Flux<Integer> dropped = flux
                .onBackpressureDrop();
            
            // onBackpressureLatest - keep latest only
            Flux<Integer> latest = flux
                .onBackpressureLatest();
            """);

        System.out.println("\n--- Reactive REST Controller ---");
        System.out.println("""
            @RestController
            @RequestMapping("/api")
            public class ReactiveController {
            
                @GetMapping("/user/{id}")
                public Mono<User> getUser(@PathVariable Long id) {
                    return userService.findById(id);
                }
                
                @GetMapping("/users")
                public Flux<User> getAllUsers() {
                    return userService.findAll();
                }
                
                @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
                public Flux<ServerSentEvent<String>> streamEvents() {
                    return Flux.interval(Duration.ofSeconds(1))
                        .map(seq -> ServerSentEvent.<String>builder()
                            .data("Event " + seq)
                            .build());
                }
            }
            """);

        System.out.println("\n--- WebClient (Reactive HTTP Client) ---");
        System.out.println("""
            @Service
            public class ApiClient {
            
                private final WebClient webClient;
                
                public ApiClient(WebClient.Builder builder) {
                    this.webClient = builder
                        .baseUrl("https://api.example.com")
                        .build();
                }
                
                public Mono<User> getUser(Long id) {
                    return webClient.get()
                        .uri("/users/{id}", id)
                        .retrieve()
                        .bodyToMono(User.class);
                }
                
                public Flux<User> getUsers() {
                    return webClient.get()
                        .uri("/users")
                        .retrieve()
                        .bodyToFlux(User.class);
                }
                
                public Mono<User> createUser(User user) {
                    return webClient.post()
                        .uri("/users")
                        .bodyValue(user)
                        .retrieve()
                        .bodyToMono(User.class);
                }
            }
            """);

        System.out.println("\nBest practices:");
        System.out.println("   ✓ Use non-blocking operations");
        System.out.println("   ✓ Handle errors with onError operators");
        System.out.println("   ✓ Use appropriate backpressure strategy");
        System.out.println("   ✓ Subscribe on appropriate scheduler");
        System.out.println("   ✗ Don't block() in production code");
        System.out.println("   ✗ Don't subscribe multiple times");
        System.out.println();
    }
}
