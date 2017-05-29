package kr.co.anajo.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private final Logger logger = LoggerFactory.getLogger(HttpServer.class);

	@Initialize
	public void startup() {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup(10);
		ChannelFuture channelFuture = null;

		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new HttpServerInitializer(null));

		try {
			Channel ch = b.bind(80).sync().channel();
			channelFuture = ch.closeFuture();
			channelFuture.sync();
		} catch (InterruptedException e) {
			logger.error("interrupted.", e);
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

}
