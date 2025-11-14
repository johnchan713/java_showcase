package com.example.demo.showcase;

/**
 * Demonstrates Apache MINA - Multipurpose Infrastructure for Network Applications
 * Covers TCP/UDP servers, clients, filters, codecs, and protocol handling
 *
 * Note: This is a demonstration - actual server/client would run separately
 */
public class ApacheMINAShowcase {

    public static void demonstrate() {
        System.out.println("\n========== APACHE MINA SHOWCASE ==========\n");

        minaOverviewDemo();
        tcpServerDemo();
        tcpClientDemo();
        udpServerDemo();
        filtersDemo();
        codecsDemo();
        advancedFeaturesDemo();
    }

    // ========== Overview ==========

    private static void minaOverviewDemo() {
        System.out.println("--- Apache MINA Overview ---");
        System.out.println("Network application framework for high-performance I/O\n");

        System.out.println("1. Key features:");
        System.out.println("   • Unified API for TCP, UDP, and serial communication");
        System.out.println("   • Event-driven, asynchronous I/O");
        System.out.println("   • Filter chain architecture (like Servlet filters)");
        System.out.println("   • Built-in SSL/TLS support");
        System.out.println("   • Protocol codec framework");
        System.out.println("   • Connection throttling and bandwidth control");
        System.out.println("   • JMX monitoring");

        System.out.println("\n2. Core components:");
        System.out.println("   • IoService: Server or client acceptor");
        System.out.println("   • IoSession: Connection session");
        System.out.println("   • IoHandler: Business logic handler");
        System.out.println("   • IoFilter: Filter chain for processing");
        System.out.println("   • ProtocolCodecFactory: Encoder/decoder");

        System.out.println("\n3. Dependencies:");
        System.out.println("""
            <dependency>
                <groupId>org.apache.mina</groupId>
                <artifactId>mina-core</artifactId>
                <version>2.2.3</version>
            </dependency>

            <!-- For codec support -->
            <dependency>
                <groupId>org.apache.mina</groupId>
                <artifactId>mina-filter-compression</artifactId>
                <version>2.2.3</version>
            </dependency>
            """);

        System.out.println();
    }

    // ========== TCP Server ==========

    private static void tcpServerDemo() {
        System.out.println("--- TCP Server ---");
        System.out.println("Build high-performance TCP servers\n");

        System.out.println("1. Basic TCP server:");
        System.out.println("""
            import org.apache.mina.core.service.IoAcceptor;
            import org.apache.mina.core.session.IdleStatus;
            import org.apache.mina.filter.codec.ProtocolCodecFilter;
            import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
            import org.apache.mina.filter.logging.LoggingFilter;
            import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

            @Component
            public class MinaTcpServer {

                private IoAcceptor acceptor;

                @PostConstruct
                public void start() throws IOException {
                    // Create NIO socket acceptor
                    acceptor = new NioSocketAcceptor();

                    // Configure the filter chain
                    acceptor.getFilterChain().addLast("logger", new LoggingFilter());
                    acceptor.getFilterChain().addLast("codec",
                        new ProtocolCodecFilter(new TextLineCodecFactory(
                            Charset.forName("UTF-8"))));

                    // Set handler
                    acceptor.setHandler(new ServerHandler());

                    // Configure socket
                    acceptor.getSessionConfig().setReadBufferSize(2048);
                    acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);

                    // Bind to port
                    acceptor.bind(new InetSocketAddress(9123));

                    System.out.println("TCP Server started on port 9123");
                }

                @PreDestroy
                public void stop() {
                    if (acceptor != null) {
                        acceptor.unbind();
                        acceptor.dispose();
                    }
                }
            }
            """);

        System.out.println("2. IoHandler implementation:");
        System.out.println("""
            import org.apache.mina.core.service.IoHandlerAdapter;
            import org.apache.mina.core.session.IoSession;

            public class ServerHandler extends IoHandlerAdapter {

                @Override
                public void sessionCreated(IoSession session) throws Exception {
                    System.out.println("Session created: " + session.getRemoteAddress());
                }

                @Override
                public void sessionOpened(IoSession session) throws Exception {
                    System.out.println("Session opened: " + session.getId());
                    session.write("Welcome to MINA Server!");
                }

                @Override
                public void messageReceived(IoSession session, Object message)
                        throws Exception {
                    String request = message.toString();
                    System.out.println("Received: " + request + " from " +
                        session.getRemoteAddress());

                    // Echo response
                    String response = "Echo: " + request;
                    session.write(response);

                    // Close on "quit"
                    if ("quit".equalsIgnoreCase(request.trim())) {
                        session.closeNow();
                    }
                }

                @Override
                public void messageSent(IoSession session, Object message)
                        throws Exception {
                    System.out.println("Sent: " + message);
                }

                @Override
                public void sessionIdle(IoSession session, IdleStatus status)
                        throws Exception {
                    System.out.println("Session idle: " + session.getId());
                    // Close idle sessions
                    session.closeNow();
                }

                @Override
                public void sessionClosed(IoSession session) throws Exception {
                    System.out.println("Session closed: " + session.getId());
                }

                @Override
                public void exceptionCaught(IoSession session, Throwable cause)
                        throws Exception {
                    System.err.println("Exception: " + cause.getMessage());
                    session.closeNow();
                }
            }
            """);

        System.out.println("3. Session management:");
        System.out.println("""
            public class SessionManager extends IoHandlerAdapter {

                private final Map<Long, IoSession> sessions = new ConcurrentHashMap<>();

                @Override
                public void sessionOpened(IoSession session) throws Exception {
                    sessions.put(session.getId(), session);

                    // Attach user data
                    session.setAttribute("username", "guest");
                    session.setAttribute("loginTime", System.currentTimeMillis());
                }

                @Override
                public void sessionClosed(IoSession session) throws Exception {
                    sessions.remove(session.getId());
                }

                // Broadcast to all sessions
                public void broadcast(String message) {
                    for (IoSession session : sessions.values()) {
                        if (session.isConnected()) {
                            session.write(message);
                        }
                    }
                }

                // Send to specific session
                public void sendToUser(Long sessionId, String message) {
                    IoSession session = sessions.get(sessionId);
                    if (session != null && session.isConnected()) {
                        session.write(message);
                    }
                }

                // Get session count
                public int getActiveSessionCount() {
                    return sessions.size();
                }
            }
            """);

        System.out.println();
    }

    // ========== TCP Client ==========

    private static void tcpClientDemo() {
        System.out.println("--- TCP Client ---");
        System.out.println("Connect to TCP servers\n");

        System.out.println("1. Basic TCP client:");
        System.out.println("""
            import org.apache.mina.core.future.ConnectFuture;
            import org.apache.mina.core.service.IoConnector;
            import org.apache.mina.transport.socket.nio.NioSocketConnector;

            @Service
            public class MinaTcpClient {

                private IoConnector connector;
                private IoSession session;

                @PostConstruct
                public void connect() {
                    // Create connector
                    connector = new NioSocketConnector();

                    // Configure filter chain
                    connector.getFilterChain().addLast("logger", new LoggingFilter());
                    connector.getFilterChain().addLast("codec",
                        new ProtocolCodecFilter(new TextLineCodecFactory(
                            Charset.forName("UTF-8"))));

                    // Set handler
                    connector.setHandler(new ClientHandler());

                    // Configure
                    connector.setConnectTimeoutMillis(10000);

                    // Connect
                    ConnectFuture future = connector.connect(
                        new InetSocketAddress("localhost", 9123)
                    );

                    future.awaitUninterruptibly();
                    session = future.getSession();

                    System.out.println("Connected to server");
                }

                public void sendMessage(String message) {
                    if (session != null && session.isConnected()) {
                        session.write(message);
                    }
                }

                @PreDestroy
                public void disconnect() {
                    if (session != null) {
                        session.closeNow();
                    }
                    if (connector != null) {
                        connector.dispose();
                    }
                }
            }
            """);

        System.out.println("2. Client handler:");
        System.out.println("""
            public class ClientHandler extends IoHandlerAdapter {

                @Override
                public void sessionOpened(IoSession session) throws Exception {
                    System.out.println("Connected to server");
                }

                @Override
                public void messageReceived(IoSession session, Object message)
                        throws Exception {
                    System.out.println("Server says: " + message);
                }

                @Override
                public void exceptionCaught(IoSession session, Throwable cause)
                        throws Exception {
                    System.err.println("Client error: " + cause.getMessage());
                }
            }
            """);

        System.out.println();
    }

    // ========== UDP Server ==========

    private static void udpServerDemo() {
        System.out.println("--- UDP Server/Client ---");
        System.out.println("Connectionless datagram communication\n");

        System.out.println("1. UDP server:");
        System.out.println("""
            import org.apache.mina.transport.socket.DatagramSessionConfig;
            import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

            @Component
            public class MinaUdpServer {

                private NioDatagramAcceptor acceptor;

                @PostConstruct
                public void start() throws IOException {
                    acceptor = new NioDatagramAcceptor();

                    // Configure
                    acceptor.getFilterChain().addLast("logger", new LoggingFilter());
                    acceptor.setHandler(new UdpHandler());

                    DatagramSessionConfig config = acceptor.getSessionConfig();
                    config.setReadBufferSize(2048);
                    config.setReuseAddress(true);

                    // Bind
                    acceptor.bind(new InetSocketAddress(9124));

                    System.out.println("UDP Server started on port 9124");
                }

                @PreDestroy
                public void stop() {
                    if (acceptor != null) {
                        acceptor.unbind();
                        acceptor.dispose();
                    }
                }
            }

            public class UdpHandler extends IoHandlerAdapter {

                @Override
                public void messageReceived(IoSession session, Object message)
                        throws Exception {
                    IoBuffer buffer = (IoBuffer) message;
                    String data = buffer.getString(Charset.forName("UTF-8").newDecoder());

                    System.out.println("UDP received: " + data);

                    // Echo back
                    IoBuffer response = IoBuffer.allocate(data.length());
                    response.put(data.getBytes("UTF-8"));
                    response.flip();
                    session.write(response);
                }
            }
            """);

        System.out.println("2. UDP client:");
        System.out.println("""
            import org.apache.mina.transport.socket.nio.NioDatagramConnector;

            public class MinaUdpClient {

                public void sendDatagram(String message) throws Exception {
                    NioDatagramConnector connector = new NioDatagramConnector();
                    connector.setHandler(new UdpClientHandler());

                    ConnectFuture future = connector.connect(
                        new InetSocketAddress("localhost", 9124)
                    );

                    future.awaitUninterruptibly();
                    IoSession session = future.getSession();

                    // Send message
                    IoBuffer buffer = IoBuffer.allocate(message.length());
                    buffer.put(message.getBytes("UTF-8"));
                    buffer.flip();
                    session.write(buffer);

                    // Wait for response
                    Thread.sleep(1000);

                    session.closeNow();
                    connector.dispose();
                }
            }
            """);

        System.out.println();
    }

    // ========== Filters ==========

    private static void filtersDemo() {
        System.out.println("--- Filters (Filter Chain) ---");
        System.out.println("Processing pipeline for I/O events\n");

        System.out.println("1. Built-in filters:");
        System.out.println("""
            import org.apache.mina.filter.compression.CompressionFilter;
            import org.apache.mina.filter.executor.ExecutorFilter;
            import org.apache.mina.filter.ssl.SslFilter;

            IoAcceptor acceptor = new NioSocketAcceptor();

            // Logging filter
            acceptor.getFilterChain().addLast("logger", new LoggingFilter());

            // SSL/TLS filter
            SslContext sslContext = createSslContext();
            acceptor.getFilterChain().addLast("ssl",
                new SslFilter(sslContext.newEngine()));

            // Compression filter
            acceptor.getFilterChain().addLast("compression",
                new CompressionFilter());

            // Executor filter (handle in thread pool)
            acceptor.getFilterChain().addLast("executor",
                new ExecutorFilter(Executors.newCachedThreadPool()));

            // Protocol codec filter
            acceptor.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new TextLineCodecFactory()));
            """);

        System.out.println("2. Custom filter:");
        System.out.println("""
            import org.apache.mina.core.filterchain.IoFilterAdapter;

            public class ThrottlingFilter extends IoFilterAdapter {

                private final RateLimiter rateLimiter;

                public ThrottlingFilter(int messagesPerSecond) {
                    this.rateLimiter = RateLimiter.create(messagesPerSecond);
                }

                @Override
                public void messageReceived(NextFilter nextFilter, IoSession session,
                                           Object message) throws Exception {
                    // Rate limiting
                    if (rateLimiter.tryAcquire()) {
                        super.messageReceived(nextFilter, session, message);
                    } else {
                        System.out.println("Rate limit exceeded, dropping message");
                    }
                }
            }

            // Use custom filter
            acceptor.getFilterChain().addLast("throttle",
                new ThrottlingFilter(100));  // 100 msgs/sec
            """);

        System.out.println("3. Authentication filter:");
        System.out.println("""
            public class AuthenticationFilter extends IoFilterAdapter {

                @Override
                public void messageReceived(NextFilter nextFilter, IoSession session,
                                           Object message) throws Exception {
                    Boolean authenticated = (Boolean) session.getAttribute("authenticated");

                    if (authenticated == null || !authenticated) {
                        // Expect authentication message
                        if (authenticate(message.toString())) {
                            session.setAttribute("authenticated", true);
                            session.write("Authentication successful");
                        } else {
                            session.write("Authentication failed");
                            session.closeNow();
                        }
                    } else {
                        // Already authenticated, pass to next filter
                        super.messageReceived(nextFilter, session, message);
                    }
                }

                private boolean authenticate(String credentials) {
                    // Validate credentials
                    return "secret".equals(credentials);
                }
            }
            """);

        System.out.println();
    }

    // ========== Codecs ==========

    private static void codecsDemo() {
        System.out.println("--- Protocol Codecs ---");
        System.out.println("Encode/decode custom protocols\n");

        System.out.println("1. Simple object codec:");
        System.out.println("""
            import org.apache.mina.core.buffer.IoBuffer;
            import org.apache.mina.core.session.IoSession;
            import org.apache.mina.filter.codec.*;

            // Message class
            public class ChatMessage {
                private String username;
                private String message;
                private long timestamp;

                // Getters, setters, constructors
            }

            // Encoder
            public class ChatMessageEncoder implements ProtocolEncoder {

                @Override
                public void encode(IoSession session, Object message,
                                  ProtocolEncoderOutput out) throws Exception {
                    ChatMessage msg = (ChatMessage) message;

                    IoBuffer buffer = IoBuffer.allocate(256);
                    buffer.setAutoExpand(true);

                    // Write fields
                    buffer.put(msg.getUsername().getBytes("UTF-8"));
                    buffer.put((byte) 0);  // Null terminator
                    buffer.put(msg.getMessage().getBytes("UTF-8"));
                    buffer.put((byte) 0);
                    buffer.putLong(msg.getTimestamp());

                    buffer.flip();
                    out.write(buffer);
                }

                @Override
                public void dispose(IoSession session) throws Exception {
                    // Cleanup
                }
            }

            // Decoder
            public class ChatMessageDecoder extends CumulativeProtocolDecoder {

                @Override
                protected boolean doDecode(IoSession session, IoBuffer in,
                                          ProtocolDecoderOutput out) throws Exception {
                    if (in.remaining() < 1) {
                        return false;  // Need more data
                    }

                    in.mark();

                    // Read username (null-terminated)
                    String username = readString(in);
                    if (username == null) {
                        in.reset();
                        return false;
                    }

                    // Read message (null-terminated)
                    String message = readString(in);
                    if (message == null) {
                        in.reset();
                        return false;
                    }

                    // Read timestamp
                    if (in.remaining() < 8) {
                        in.reset();
                        return false;
                    }
                    long timestamp = in.getLong();

                    // Create and output message
                    ChatMessage chatMsg = new ChatMessage(username, message, timestamp);
                    out.write(chatMsg);

                    return true;  // Message decoded
                }

                private String readString(IoBuffer in) {
                    StringBuilder sb = new StringBuilder();
                    while (in.hasRemaining()) {
                        byte b = in.get();
                        if (b == 0) {
                            return sb.toString();
                        }
                        sb.append((char) b);
                    }
                    return null;  // Incomplete
                }
            }

            // Codec factory
            public class ChatMessageCodecFactory implements ProtocolCodecFactory {

                @Override
                public ProtocolEncoder getEncoder(IoSession session) {
                    return new ChatMessageEncoder();
                }

                @Override
                public ProtocolDecoder getDecoder(IoSession session) {
                    return new ChatMessageDecoder();
                }
            }

            // Use codec
            acceptor.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new ChatMessageCodecFactory()));
            """);

        System.out.println();
    }

    // ========== Advanced Features ==========

    private static void advancedFeaturesDemo() {
        System.out.println("--- Advanced Features ---");
        System.out.println("SSL, statistics, and best practices\n");

        System.out.println("1. SSL/TLS support:");
        System.out.println("""
            import org.apache.mina.filter.ssl.SslFilter;

            // Create SSL context
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream("keystore.jks"), "password".toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(keyStore, "password".toCharArray());

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, null);

            // Add SSL filter
            SslFilter sslFilter = new SslFilter(sslContext);
            sslFilter.setUseClientMode(false);  // Server mode

            acceptor.getFilterChain().addFirst("ssl", sslFilter);
            """);

        System.out.println("2. Statistics and monitoring:");
        System.out.println("""
            import org.apache.mina.core.service.IoServiceStatistics;

            IoAcceptor acceptor = new NioSocketAcceptor();

            // Enable statistics
            IoServiceStatistics stats = acceptor.getStatistics();
            stats.setThroughputCalculationInterval(30);  // 30 seconds

            // Read statistics
            long totalSessions = stats.getCumulativeManagedSessionCount();
            long currentSessions = acceptor.getManagedSessionCount();
            double readBytes = stats.getReadBytesThroughput();
            double writtenBytes = stats.getWrittenBytesThroughput();
            double readMessages = stats.getReadMessagesThroughput();
            double writtenMessages = stats.getWrittenMessagesThroughput();

            System.out.println("Total sessions: " + totalSessions);
            System.out.println("Active sessions: " + currentSessions);
            System.out.println("Read throughput: " + readBytes + " bytes/sec");
            System.out.println("Write throughput: " + writtenBytes + " bytes/sec");
            """);

        System.out.println("3. Connection throttling:");
        System.out.println("""
            // Limit maximum sessions
            acceptor.setDefaultLocalAddress(new InetSocketAddress(9123));
            acceptor.getSessionConfig().setMaxReadBufferSize(2048);

            // Custom acceptor to limit connections
            public class ThrottlingAcceptor extends NioSocketAcceptor {

                private static final int MAX_CONNECTIONS = 1000;

                @Override
                protected void finishSessionInitialization(IoSession session,
                        IoFuture future, IoSessionInitializer sessionInitializer) {

                    if (getManagedSessionCount() >= MAX_CONNECTIONS) {
                        session.closeNow();
                        return;
                    }

                    super.finishSessionInitialization(session, future, sessionInitializer);
                }
            }
            """);

        System.out.println("4. Best practices:");
        System.out.println("   ✓ Use filter chain for separation of concerns");
        System.out.println("   ✓ Implement proper error handling in handlers");
        System.out.println("   ✓ Set idle timeout to clean up stale sessions");
        System.out.println("   ✓ Use ExecutorFilter for CPU-intensive tasks");
        System.out.println("   ✓ Configure read/write buffer sizes appropriately");
        System.out.println("   ✓ Monitor session count and throughput");
        System.out.println("   ✓ Use SSL/TLS for secure communication");
        System.out.println("   ✓ Implement authentication and authorization");
        System.out.println("   ✗ Don't block in IoHandler methods");
        System.out.println("   ✗ Don't forget to dispose resources");
        System.out.println("   ✗ Don't use synchronous writes in handler");

        System.out.println("\n5. Common use cases:");
        System.out.println("   • Chat servers");
        System.out.println("   • Game servers");
        System.out.println("   • IoT device communication");
        System.out.println("   • Custom protocol implementations");
        System.out.println("   • Proxy servers");
        System.out.println("   • Network monitoring tools");

        System.out.println();
    }
}
