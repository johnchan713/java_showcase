package com.example.demo.showcase;

/**
 * Demonstrates WebSocket with Spring Boot
 * Covers STOMP protocol, message brokers, subscriptions, and real-time communication
 */
public class WebSocketShowcase {

    public static void demonstrate() {
        System.out.println("\n========== WEBSOCKET SHOWCASE ==========\n");

        webSocketOverviewDemo();
        stompConfigurationDemo();
        messageHandlersDemo();
        clientSubscriptionsDemo();
        advancedFeaturesDemo();
    }

    // ========== Overview ==========

    private static void webSocketOverviewDemo() {
        System.out.println("--- WebSocket Overview ---");
        System.out.println("Real-time bidirectional communication\n");

        System.out.println("1. Key concepts:");
        System.out.println("   • WebSocket: Full-duplex communication protocol");
        System.out.println("   • STOMP: Simple Text Oriented Messaging Protocol");
        System.out.println("   • Message Broker: Routes messages to subscribers");
        System.out.println("   • Topic: Broadcast to all subscribers");
        System.out.println("   • Queue: Point-to-point messaging");
        System.out.println("   • SockJS: WebSocket fallback for old browsers");

        System.out.println("\n2. Use cases:");
        System.out.println("   • Chat applications");
        System.out.println("   • Real-time notifications");
        System.out.println("   • Live sports scores");
        System.out.println("   • Collaborative editing");
        System.out.println("   • Stock tickers");
        System.out.println("   • Gaming");

        System.out.println("\n3. Dependencies:");
        System.out.println("""
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-websocket</artifactId>
            </dependency>
            """);

        System.out.println();
    }

    // ========== STOMP Configuration ==========

    private static void stompConfigurationDemo() {
        System.out.println("--- STOMP Configuration ---");
        System.out.println("Configure WebSocket message broker\n");

        System.out.println("1. Basic configuration:");
        System.out.println("""
            import org.springframework.context.annotation.Configuration;
            import org.springframework.messaging.simp.config.MessageBrokerRegistry;
            import org.springframework.web.socket.config.annotation.*;

            @Configuration
            @EnableWebSocketMessageBroker
            public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

                @Override
                public void configureMessageBroker(MessageBrokerRegistry config) {
                    // Enable simple in-memory broker
                    config.enableSimpleBroker("/topic", "/queue");

                    // Application destination prefix
                    config.setApplicationDestinationPrefixes("/app");

                    // User destination prefix (for private messages)
                    config.setUserDestinationPrefix("/user");
                }

                @Override
                public void registerStompEndpoints(StompEndpointRegistry registry) {
                    // WebSocket endpoint
                    registry.addEndpoint("/ws")
                        .setAllowedOrigins("*")
                        .withSockJS();  // SockJS fallback

                    // Without SockJS
                    registry.addEndpoint("/ws-native")
                        .setAllowedOrigins("http://localhost:3000");
                }
            }
            """);

        System.out.println("2. External message broker (RabbitMQ/ActiveMQ):");
        System.out.println("""
            @Override
            public void configureMessageBroker(MessageBrokerRegistry config) {
                // Enable STOMP broker relay
                config.enableStompBrokerRelay("/topic", "/queue")
                    .setRelayHost("localhost")
                    .setRelayPort(61613)
                    .setClientLogin("guest")
                    .setClientPasscode("guest")
                    .setSystemLogin("guest")
                    .setSystemPasscode("guest");

                config.setApplicationDestinationPrefixes("/app");
            }
            """);

        System.out.println("3. Custom interceptors:");
        System.out.println("""
            @Override
            public void configureClientInboundChannel(ChannelRegistration registration) {
                registration.interceptors(new ChannelInterceptor() {
                    @Override
                    public Message<?> preSend(Message<?> message, MessageChannel channel) {
                        StompHeaderAccessor accessor =
                            MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                            String authToken = accessor.getFirstNativeHeader("Authorization");
                            // Validate token
                            if (isValidToken(authToken)) {
                                accessor.setUser(new UserPrincipal(getUserFromToken(authToken)));
                            }
                        }

                        return message;
                    }
                });
            }
            """);

        System.out.println();
    }

    // ========== Message Handlers ==========

    private static void messageHandlersDemo() {
        System.out.println("--- Message Handlers ---");
        System.out.println("Handle incoming WebSocket messages\n");

        System.out.println("1. Basic message handler:");
        System.out.println("""
            import org.springframework.messaging.handler.annotation.*;
            import org.springframework.messaging.simp.SimpMessagingTemplate;
            import org.springframework.stereotype.Controller;

            @Controller
            public class ChatController {

                @Autowired
                private SimpMessagingTemplate messagingTemplate;

                // Handle messages sent to /app/chat.send
                @MessageMapping("/chat.send")
                @SendTo("/topic/messages")
                public ChatMessage sendMessage(ChatMessage message) {
                    message.setTimestamp(System.currentTimeMillis());
                    return message;  // Broadcast to all subscribers of /topic/messages
                }

                // Multiple destinations
                @MessageMapping("/notification")
                @SendTo({"/topic/notifications", "/topic/all"})
                public Notification sendNotification(Notification notification) {
                    return notification;
                }
            }

            // Message model
            public class ChatMessage {
                private String content;
                private String sender;
                private MessageType type;
                private long timestamp;

                public enum MessageType {
                    CHAT, JOIN, LEAVE
                }

                // Getters, setters
            }
            """);

        System.out.println("2. Private messages (user-specific):");
        System.out.println("""
            @MessageMapping("/chat.private")
            public void sendPrivateMessage(
                    @Payload ChatMessage message,
                    @Header("simpSessionId") String sessionId,
                    Principal principal) {

                String username = principal.getName();
                message.setSender(username);

                // Send to specific user
                messagingTemplate.convertAndSendToUser(
                    message.getRecipient(),
                    "/queue/private",
                    message
                );
            }

            // Client subscribes to: /user/queue/private
            """);

        System.out.println("3. Message headers and validation:");
        System.out.println("""
            @MessageMapping("/order.place")
            @SendTo("/topic/orders")
            public OrderResponse placeOrder(
                    @Payload @Valid Order order,
                    @Header("simpSessionId") String sessionId,
                    @Headers Map<String, Object> headers,
                    Principal principal) {

                String username = principal.getName();
                System.out.println("User: " + username);
                System.out.println("Session: " + sessionId);

                // Process order
                OrderResponse response = orderService.placeOrder(order, username);

                return response;
            }
            """);

        System.out.println("4. Exception handling:");
        System.out.println("""
            @MessageExceptionHandler
            @SendToUser("/queue/errors")
            public ErrorMessage handleException(Exception exception) {
                return new ErrorMessage(exception.getMessage());
            }

            @MessageExceptionHandler(ValidationException.class)
            @SendTo("/topic/errors")
            public ErrorMessage handleValidationError(ValidationException ex) {
                return new ErrorMessage("Validation failed: " + ex.getMessage());
            }
            """);

        System.out.println();
    }

    // ========== Client Subscriptions ==========

    private static void clientSubscriptionsDemo() {
        System.out.println("--- Client Subscriptions ---");
        System.out.println("JavaScript client examples\n");

        System.out.println("1. Connect and subscribe (SockJS + STOMP):");
        System.out.println("""
            // JavaScript client

            // Include libraries
            <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/stompjs@2/lib/stomp.min.js"></script>

            // Connect
            const socket = new SockJS('/ws');
            const stompClient = Stomp.over(socket);

            stompClient.connect({}, function(frame) {
                console.log('Connected: ' + frame);

                // Subscribe to topic
                stompClient.subscribe('/topic/messages', function(message) {
                    const chatMessage = JSON.parse(message.body);
                    showMessage(chatMessage);
                });

                // Subscribe to private queue
                stompClient.subscribe('/user/queue/private', function(message) {
                    const privateMessage = JSON.parse(message.body);
                    showPrivateMessage(privateMessage);
                });
            });
            """);

        System.out.println("2. Send messages:");
        System.out.println("""
            // Send message
            function sendMessage() {
                const message = {
                    content: document.getElementById('message').value,
                    sender: username,
                    type: 'CHAT'
                };

                stompClient.send("/app/chat.send",
                    {},
                    JSON.stringify(message)
                );
            }

            // Send with headers
            function sendPrivateMessage(recipient, content) {
                const message = {
                    content: content,
                    recipient: recipient
                };

                stompClient.send("/app/chat.private",
                    { 'priority': 'high' },
                    JSON.stringify(message)
                );
            }

            // Disconnect
            function disconnect() {
                if (stompClient !== null) {
                    stompClient.disconnect();
                }
                console.log("Disconnected");
            }
            """);

        System.out.println("3. Native WebSocket (without SockJS):");
        System.out.println("""
            const socket = new WebSocket('ws://localhost:8080/ws-native');
            const client = Stomp.over(socket);

            client.connect({}, function(frame) {
                console.log('Connected');

                client.subscribe('/topic/messages', function(message) {
                    console.log('Received: ' + message.body);
                });
            });
            """);

        System.out.println("4. React example:");
        System.out.println("""
            import SockJS from 'sockjs-client';
            import Stomp from 'stompjs';

            function ChatComponent() {
                const [stompClient, setStompClient] = useState(null);
                const [messages, setMessages] = useState([]);

                useEffect(() => {
                    const socket = new SockJS('/ws');
                    const client = Stomp.over(socket);

                    client.connect({}, () => {
                        client.subscribe('/topic/messages', (message) => {
                            const chatMessage = JSON.parse(message.body);
                            setMessages(prev => [...prev, chatMessage]);
                        });
                    });

                    setStompClient(client);

                    return () => {
                        if (client) {
                            client.disconnect();
                        }
                    };
                }, []);

                const sendMessage = (content) => {
                    if (stompClient) {
                        stompClient.send('/app/chat.send', {},
                            JSON.stringify({ content, type: 'CHAT' }));
                    }
                };

                return (
                    <div>
                        {messages.map((msg, i) => <div key={i}>{msg.content}</div>)}
                    </div>
                );
            }
            """);

        System.out.println();
    }

    // ========== Advanced Features ==========

    private static void advancedFeaturesDemo() {
        System.out.println("--- Advanced Features ---");
        System.out.println("Event listeners, broadcasting, and best practices\n");

        System.out.println("1. Event listeners:");
        System.out.println("""
            import org.springframework.context.event.EventListener;
            import org.springframework.web.socket.messaging.*;

            @Component
            public class WebSocketEventListener {

                @Autowired
                private SimpMessagingTemplate messagingTemplate;

                @EventListener
                public void handleWebSocketConnectListener(SessionConnectedEvent event) {
                    StompHeaderAccessor headerAccessor =
                        StompHeaderAccessor.wrap(event.getMessage());

                    String sessionId = headerAccessor.getSessionId();
                    System.out.println("New WebSocket connection: " + sessionId);
                }

                @EventListener
                public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
                    StompHeaderAccessor headerAccessor =
                        StompHeaderAccessor.wrap(event.getMessage());

                    String username = (String) headerAccessor.getSessionAttributes()
                        .get("username");

                    if (username != null) {
                        System.out.println("User disconnected: " + username);

                        // Broadcast leave message
                        ChatMessage leaveMessage = new ChatMessage();
                        leaveMessage.setType(MessageType.LEAVE);
                        leaveMessage.setSender(username);

                        messagingTemplate.convertAndSend("/topic/messages", leaveMessage);
                    }
                }

                @EventListener
                public void handleSubscribeEvent(SessionSubscribeEvent event) {
                    StompHeaderAccessor headerAccessor =
                        StompHeaderAccessor.wrap(event.getMessage());

                    String destination = headerAccessor.getDestination();
                    System.out.println("Subscribed to: " + destination);
                }
            }
            """);

        System.out.println("2. Broadcasting with SimpMessagingTemplate:");
        System.out.println("""
            @Service
            public class NotificationService {

                @Autowired
                private SimpMessagingTemplate messagingTemplate;

                // Broadcast to topic
                public void sendGlobalNotification(String message) {
                    Notification notification = new Notification(message);
                    messagingTemplate.convertAndSend("/topic/notifications", notification);
                }

                // Send to specific user
                public void sendPersonalNotification(String username, String message) {
                    Notification notification = new Notification(message);
                    messagingTemplate.convertAndSendToUser(
                        username,
                        "/queue/notifications",
                        notification
                    );
                }

                // Schedule periodic broadcasts
                @Scheduled(fixedRate = 5000)
                public void sendPeriodicUpdate() {
                    SystemStatus status = getSystemStatus();
                    messagingTemplate.convertAndSend("/topic/system", status);
                }
            }
            """);

        System.out.println("3. Session management:");
        System.out.println("""
            @Service
            public class SessionService {

                private final Map<String, SessionInfo> sessions = new ConcurrentHashMap<>();

                @EventListener
                public void handleConnect(SessionConnectedEvent event) {
                    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
                    String sessionId = accessor.getSessionId();

                    SessionInfo info = new SessionInfo();
                    info.setSessionId(sessionId);
                    info.setConnectedAt(System.currentTimeMillis());

                    sessions.put(sessionId, info);
                }

                @EventListener
                public void handleDisconnect(SessionDisconnectEvent event) {
                    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
                    sessions.remove(accessor.getSessionId());
                }

                public int getActiveSessionCount() {
                    return sessions.size();
                }

                public List<SessionInfo> getActiveSessions() {
                    return new ArrayList<>(sessions.values());
                }
            }
            """);

        System.out.println("4. Security:");
        System.out.println("""
            @Configuration
            public class WebSocketSecurityConfig {

                @Bean
                public SecurityFilterChain securityFilterChain(HttpSecurity http)
                        throws Exception {
                    http
                        .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/ws/**").permitAll()
                        )
                        .csrf(csrf -> csrf.disable());

                    return http.build();
                }
            }

            // Message-level security
            @Configuration
            @EnableWebSocketMessageBroker
            public class WebSocketConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

                @Override
                protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
                    messages
                        .simpDestMatchers("/app/admin/**").hasRole("ADMIN")
                        .simpDestMatchers("/app/chat/**").authenticated()
                        .simpSubscribeDestMatchers("/topic/**").permitAll()
                        .anyMessage().authenticated();
                }

                @Override
                protected boolean sameOriginDisabled() {
                    return true;  // Disable CSRF for WebSocket
                }
            }
            """);

        System.out.println("5. Best practices:");
        System.out.println("   ✓ Use SockJS for broader browser support");
        System.out.println("   ✓ Implement authentication/authorization");
        System.out.println("   ✓ Handle connection/disconnection events");
        System.out.println("   ✓ Use topics for broadcast, queues for private messages");
        System.out.println("   ✓ Validate messages on server side");
        System.out.println("   ✓ Monitor active sessions and connections");
        System.out.println("   ✓ Implement reconnection logic on client");
        System.out.println("   ✓ Use external broker (RabbitMQ) for production");
        System.out.println("   ✗ Don't send sensitive data without encryption");
        System.out.println("   ✗ Don't broadcast unnecessary data");
        System.out.println("   ✗ Don't forget to clean up on disconnect");

        System.out.println("\n6. Testing:");
        System.out.println("""
            @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
            public class WebSocketTest {

                @LocalServerPort
                private int port;

                private WebSocketStompClient stompClient;

                @BeforeEach
                public void setup() {
                    WebSocketClient client = new StandardWebSocketClient();
                    stompClient = new WebSocketStompClient(client);
                    stompClient.setMessageConverter(new MappingJackson2MessageConverter());
                }

                @Test
                public void testSendMessage() throws Exception {
                    CompletableFuture<ChatMessage> future = new CompletableFuture<>();

                    StompSession session = stompClient
                        .connect("ws://localhost:" + port + "/ws", new StompSessionHandlerAdapter() {})
                        .get(1, TimeUnit.SECONDS);

                    session.subscribe("/topic/messages", new StompFrameHandler() {
                        @Override
                        public Type getPayloadType(StompHeaders headers) {
                            return ChatMessage.class;
                        }

                        @Override
                        public void handleFrame(StompHeaders headers, Object payload) {
                            future.complete((ChatMessage) payload);
                        }
                    });

                    ChatMessage message = new ChatMessage("Hello", "Test");
                    session.send("/app/chat.send", message);

                    ChatMessage received = future.get(5, TimeUnit.SECONDS);
                    assertEquals("Hello", received.getContent());
                }
            }
            """);

        System.out.println();
    }
}
