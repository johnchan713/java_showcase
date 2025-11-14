package com.example.demo.showcase;

/**
 * Demonstrates Netty - Asynchronous event-driven network framework
 * Covers servers, clients, codecs, and handlers
 */
public class NettyShowcase {

    public static void demonstrate() {
        System.out.println("\n========== NETTY SHOWCASE ==========\n");

        System.out.println("--- Netty Overview ---");
        System.out.println("High-performance async network framework\n");

        System.out.println("Dependencies:");
        System.out.println("""
            <dependency>
                <groupId>io.netty</groupId>
                <artifactId>netty-all</artifactId>
                <version>4.1.104.Final</version>
            </dependency>
            """);

        System.out.println("\n--- TCP Server ---");
        System.out.println("""
            import io.netty.bootstrap.ServerBootstrap;
            import io.netty.channel.*;
            import io.netty.channel.nio.NioEventLoopGroup;
            import io.netty.channel.socket.SocketChannel;
            import io.netty.channel.socket.nio.NioServerSocketChannel;
            
            public class NettyServer {
            
                public void start(int port) throws Exception {
                    EventLoopGroup bossGroup = new NioEventLoopGroup(1);
                    EventLoopGroup workerGroup = new NioEventLoopGroup();
                    
                    try {
                        ServerBootstrap bootstrap = new ServerBootstrap();
                        bootstrap.group(bossGroup, workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) {
                                    ch.pipeline().addLast(
                                        new StringDecoder(),
                                        new StringEncoder(),
                                        new ServerHandler()
                                    );
                                }
                            })
                            .option(ChannelOption.SO_BACKLOG, 128)
                            .childOption(ChannelOption.SO_KEEPALIVE, true);
                        
                        ChannelFuture future = bootstrap.bind(port).sync();
                        System.out.println("Server started on port " + port);
                        
                        future.channel().closeFuture().sync();
                        
                    } finally {
                        workerGroup.shutdownGracefully();
                        bossGroup.shutdownGracefully();
                    }
                }
            }
            """);

        System.out.println("\n--- Channel Handler ---");
        System.out.println("""
            public class ServerHandler extends ChannelInboundHandlerAdapter {
            
                @Override
                public void channelRead(ChannelHandlerContext ctx, Object msg) {
                    String message = (String) msg;
                    System.out.println("Received: " + message);
                    
                    // Echo response
                    ctx.writeAndFlush("Echo: " + message);
                }
                
                @Override
                public void channelActive(ChannelHandlerContext ctx) {
                    System.out.println("Client connected: " + ctx.channel().remoteAddress());
                }
                
                @Override
                public void channelInactive(ChannelHandlerContext ctx) {
                    System.out.println("Client disconnected");
                }
                
                @Override
                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                    cause.printStackTrace();
                    ctx.close();
                }
            }
            """);

        System.out.println("\n--- TCP Client ---");
        System.out.println("""
            public class NettyClient {
            
                public void connect(String host, int port) throws Exception {
                    EventLoopGroup group = new NioEventLoopGroup();
                    
                    try {
                        Bootstrap bootstrap = new Bootstrap();
                        bootstrap.group(group)
                            .channel(NioSocketChannel.class)
                            .handler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) {
                                    ch.pipeline().addLast(
                                        new StringDecoder(),
                                        new StringEncoder(),
                                        new ClientHandler()
                                    );
                                }
                            });
                        
                        ChannelFuture future = bootstrap.connect(host, port).sync();
                        
                        // Send message
                        future.channel().writeAndFlush("Hello Server");
                        
                        future.channel().closeFuture().sync();
                        
                    } finally {
                        group.shutdownGracefully();
                    }
                }
            }
            """);

        System.out.println("\n--- HTTP Server ---");
        System.out.println("""
            public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
            
                @Override
                protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
                    String uri = request.uri();
                    HttpMethod method = request.method();
                    
                    String responseContent = "Hello from Netty HTTP Server!";
                    
                    FullHttpResponse response = new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        HttpResponseStatus.OK,
                        Unpooled.copiedBuffer(responseContent, CharsetUtil.UTF_8)
                    );
                    
                    response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
                    response.headers().set(HttpHeaderNames.CONTENT_LENGTH,
                        response.content().readableBytes());
                    
                    ctx.writeAndFlush(response);
                }
            }
            
            // In pipeline
            ch.pipeline().addLast(
                new HttpServerCodec(),
                new HttpObjectAggregator(65536),
                new HttpServerHandler()
            );
            """);

        System.out.println("\n--- WebSocket Server ---");
        System.out.println("""
            public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
            
                @Override
                protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
                    String text = frame.text();
                    System.out.println("Received: " + text);
                    
                    // Echo back
                    ctx.channel().writeAndFlush(new TextWebSocketFrame("Echo: " + text));
                }
            }
            
            // In pipeline
            ch.pipeline().addLast(
                new HttpServerCodec(),
                new HttpObjectAggregator(65536),
                new WebSocketServerProtocolHandler("/ws"),
                new WebSocketHandler()
            );
            """);

        System.out.println("\nBest practices:");
        System.out.println("   ✓ Use EventLoopGroup appropriately");
        System.out.println("   ✓ Shutdown groups gracefully");
        System.out.println("   ✓ Use ByteBuf pooling");
        System.out.println("   ✓ Handle exceptions properly");
        System.out.println("   ✗ Don't block in handlers");
        System.out.println();
    }
}
