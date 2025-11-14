package com.example.demo.showcase;

/**
 * Demonstrates QuickFIX/J - FIX Protocol Engine
 * Covers FIX messaging, session management, and trading applications
 */
public class QuickFIXJShowcase {

    public static void demonstrate() {
        System.out.println("\n========== QUICKFIX/J SHOWCASE ==========\n");

        System.out.println("--- QuickFIX/J Overview ---");
        System.out.println("FIX (Financial Information eXchange) Protocol Engine\n");

        System.out.println("Key concepts:");
        System.out.println("   • FIX Protocol: Standard for electronic trading");
        System.out.println("   • Session: Connection between counterparties");
        System.out.println("   • Message Types: New Order, Execution Report, etc.");
        System.out.println("   • Tags: FIX field identifiers");
        System.out.println("   • Sequence Numbers: Message ordering");

        System.out.println("\nDependencies:");
        System.out.println("""
            <dependency>
                <groupId>org.quickfixj</groupId>
                <artifactId>quickfixj-core</artifactId>
                <version>2.3.1</version>
            </dependency>
            <dependency>
                <groupId>org.quickfixj</groupId>
                <artifactId>quickfixj-messages-all</artifactId>
                <version>2.3.1</version>
            </dependency>
            """);

        System.out.println("\n--- Configuration File ---");
        System.out.println("""
            # quickfixj.cfg (Initiator configuration)
            [DEFAULT]
            ConnectionType=initiator
            ReconnectInterval=30
            FileStorePath=data/fixstore
            FileLogPath=data/fixlogs
            StartTime=00:00:00
            EndTime=23:59:59
            HeartBtInt=30
            SocketConnectPort=5001
            SocketConnectHost=localhost
            SenderCompID=CLIENT
            TargetCompID=SERVER
            
            [SESSION]
            BeginString=FIX.4.4
            DataDictionary=FIX44.xml
            """);

        System.out.println("\n--- Application Implementation ---");
        System.out.println("""
            import quickfix.*;
            import quickfix.field.*;
            import quickfix.fix44.*;
            
            public class TradingApplication extends MessageCracker
                    implements Application {
            
                @Override
                public void onCreate(SessionID sessionID) {
                    System.out.println("Session created: " + sessionID);
                }
            
                @Override
                public void onLogon(SessionID sessionID) {
                    System.out.println("Logon: " + sessionID);
                }
            
                @Override
                public void onLogout(SessionID sessionID) {
                    System.out.println("Logout: " + sessionID);
                }
            
                @Override
                public void toAdmin(Message message, SessionID sessionID) {
                    // Administrative messages (Logon, Heartbeat, etc.)
                    System.out.println("Admin: " + message);
                }
            
                @Override
                public void fromAdmin(Message message, SessionID sessionID) {
                    System.out.println("From Admin: " + message);
                }
            
                @Override
                public void toApp(Message message, SessionID sessionID)
                        throws DoNotSend {
                    // Application messages going out
                    crack(message, sessionID);
                }
            
                @Override
                public void fromApp(Message message, SessionID sessionID)
                        throws FieldNotFound, UnsupportedMessageType,
                               IncorrectTagValue {
                    // Application messages coming in
                    crack(message, sessionID);
                }
            
                // Handle Execution Reports
                @Handler
                public void onMessage(ExecutionReport message, SessionID sessionID)
                        throws FieldNotFound {
                    String orderId = message.getString(ClOrdID.FIELD);
                    char orderStatus = message.getChar(OrdStatus.FIELD);
                    double price = message.getDouble(Price.FIELD);
                    
                    System.out.println("Execution Report:");
                    System.out.println("  Order ID: " + orderId);
                    System.out.println("  Status: " + orderStatus);
                    System.out.println("  Price: " + price);
                }
            }
            """);

        System.out.println("\n--- Starting Initiator ---");
        System.out.println("""
            public class FixInitiator {
                public static void main(String[] args) throws Exception {
                    SessionSettings settings = 
                        new SessionSettings("quickfixj.cfg");
                    
                    Application application = new TradingApplication();
                    MessageStoreFactory storeFactory = 
                        new FileStoreFactory(settings);
                    LogFactory logFactory = new FileLogFactory(settings);
                    MessageFactory messageFactory = new DefaultMessageFactory();
                    
                    Initiator initiator = new SocketInitiator(
                        application,
                        storeFactory,
                        settings,
                        logFactory,
                        messageFactory
                    );
                    
                    initiator.start();
                    
                    // Keep running
                    Thread.sleep(60000);
                    
                    initiator.stop();
                }
            }
            """);

        System.out.println("\n--- Sending New Order ---");
        System.out.println("""
            public void sendNewOrder(SessionID sessionID) throws SessionNotFound {
                NewOrderSingle order = new NewOrderSingle(
                    new ClOrdID("ORDER123"),
                    new Side(Side.BUY),
                    new TransactTime(),
                    new OrdType(OrdType.LIMIT)
                );
                
                order.set(new Symbol("AAPL"));
                order.set(new OrderQty(100));
                order.set(new Price(150.00));
                order.set(new TimeInForce(TimeInForce.DAY));
                
                Session.sendToTarget(order, sessionID);
            }
            """);

        System.out.println("\n--- Common FIX Message Types ---");
        System.out.println("""
            // Logon (MsgType=A)
            Logon logon = new Logon();
            logon.set(new EncryptMethod(EncryptMethod.NONE));
            logon.set(new HeartBtInt(30));
            
            // Heartbeat (MsgType=0)
            Heartbeat heartbeat = new Heartbeat();
            
            // New Order Single (MsgType=D)
            NewOrderSingle order = new NewOrderSingle(...);
            
            // Order Cancel Request (MsgType=F)
            OrderCancelRequest cancel = new OrderCancelRequest(
                new OrigClOrdID("ORDER123"),
                new ClOrdID("CANCEL456"),
                new Side(Side.BUY),
                new TransactTime()
            );
            
            // Market Data Request (MsgType=V)
            MarketDataRequest mdRequest = new MarketDataRequest(
                new MDReqID("MD001"),
                new SubscriptionRequestType(
                    SubscriptionRequestType.SNAPSHOT_PLUS_UPDATES
                ),
                new MarketDepth(1)
            );
            """);

        System.out.println("\nBest practices:");
        System.out.println("   ✓ Handle session lifecycle events");
        System.out.println("   ✓ Implement message validation");
        System.out.println("   ✓ Use file store for persistence");
        System.out.println("   ✓ Monitor sequence numbers");
        System.out.println("   ✓ Configure heartbeat intervals");
        System.out.println();
    }
}
