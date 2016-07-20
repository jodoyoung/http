package kr.co.anajo.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import kr.co.anajo.context.annotation.Component;
import kr.co.anajo.context.annotation.Initialize;

@Component
public class HttpServer {

	@Initialize
	public void startup() {
		Thread serverThread = new Thread(() -> {
			EventLoopGroup bossGroup = new NioEventLoopGroup(1);
			EventLoopGroup workerGroup = new NioEventLoopGroup(10);
			ChannelFuture channelFuture = null;

			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO)).childHandler(new HttpServerInitializer(null));

			try {
				Channel ch = b.bind(8080).sync().channel();
				channelFuture = ch.closeFuture();
				channelFuture.sync();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				bossGroup.shutdownGracefully();
				workerGroup.shutdownGracefully();
			}
		});
		serverThread.setDaemon(true);
		serverThread.start();
	}

}
