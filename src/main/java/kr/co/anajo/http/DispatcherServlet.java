package kr.co.anajo.http;

import java.util.logging.Logger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;

public class DispatcherServlet extends SimpleChannelInboundHandler<FullHttpMessage> {

	private final Logger logger = Logger.getLogger(DispatcherServlet.class.getName());

	private HttpRequest request;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpMessage msg) throws Exception {
		if (msg instanceof HttpRequest) {
			this.request = (HttpRequest) msg;

			if (HttpUtil.is100ContinueExpected(msg)) {
				ctx.write(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE));
			}

			HttpHeaders headers = request.headers();
			String uri = request.uri();
			HttpMethod method = request.method();
			System.out.println("HTTP: " + headers);
			System.out.println("HTTP: " + uri);
			System.out.println("HTTP: " + method);
		}

	}

}
