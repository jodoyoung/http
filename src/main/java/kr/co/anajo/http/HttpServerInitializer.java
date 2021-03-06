package kr.co.anajo.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;
import kr.co.anajo.http.handler.AuthenticationHandler;
import kr.co.anajo.http.handler.DispatcherHandler;
import kr.co.anajo.http.handler.StaticResourceHandler;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

	private final SslContext sslCtx;

	public HttpServerInitializer(SslContext sslCtx) {
		this.sslCtx = sslCtx;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		if (this.sslCtx != null) {
			pipeline.addLast(sslCtx.newHandler(ch.alloc()));
		}

		pipeline.addLast(new HttpServerCodec());
		pipeline.addLast(new HttpObjectAggregator(65536));
		pipeline.addLast(new ChunkedWriteHandler());
		pipeline.addLast(new AuthenticationHandler());
		pipeline.addLast(new StaticResourceHandler());
		pipeline.addLast(new DispatcherHandler());
	}

}
