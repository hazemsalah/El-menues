package com.server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class HttpServer {
public static void main (String []args) {
	EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

	try {
	    ServerBootstrap bootstrap = new ServerBootstrap()
	        .group(eventLoopGroup)
	        .handler(new LoggingHandler(LogLevel.INFO))
	        .childHandler(new HttpServerInitializer())
	        .channel(NioServerSocketChannel.class);

	    Channel ch = bootstrap.bind(8090).sync().channel();
	    ch.closeFuture().sync();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} finally {
	    eventLoopGroup.shutdownGracefully();

}
}
}
