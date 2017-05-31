package kr.co.anajo.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import kr.co.anajo.context.annotation.Component;
import kr.co.anajo.context.annotation.Initialize;

@Component
public class HttpServer {

	private final Logger logger = LoggerFactory.getLogger(HttpServer.class);

	@Initialize
	public void startup() {
		new Thread(() -> {
			EventLoopGroup bossGroup = new NioEventLoopGroup(1);
			EventLoopGroup workerGroup = new NioEventLoopGroup(5);
			EventExecutorGroup executorGroup = new DefaultEventExecutorGroup(10);

			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO)).childHandler(new HttpServerInitializer(null));

			try {
				ChannelFuture channelFuture = b.bind(80).sync();
				channelFuture.channel().closeFuture().sync();
			} catch (InterruptedException e) {
				logger.error("interrupted.", e);
			} finally {
				bossGroup.shutdownGracefully();
				workerGroup.shutdownGracefully();
				executorGroup.shutdownGracefully();
			}
		}).start();
	}

}
