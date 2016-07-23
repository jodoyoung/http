package kr.co.anajo.http;

import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.AsciiString;

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
			boolean isKeepAlive = HttpUtil.isKeepAlive(request);
			FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
					Unpooled.wrappedBuffer("11111111".getBytes()));
			response.headers().set(new AsciiString("Content-Type"), "text/plain");
			response.headers().set(new AsciiString("Content-Length"), response.content().readableBytes());

			if (HttpUtil.isKeepAlive(request)) {
				ctx.write(response).addListener(ChannelFutureListener.CLOSE);
			} else {
				response.headers().set(new AsciiString("Connection"), new AsciiString("keep-alive"));
				ctx.write(response);
			}

			// HttpHeaders headers = request.headers();
			// String uri = request.uri();
			// HttpMethod method = request.method();
			// System.out.println("HTTP: " + headers);
			// System.out.println("HTTP: " + uri);
			// System.out.println("HTTP: " + method);
		}

		if (msg instanceof HttpContent) {
			HttpContent httpContent = (HttpContent) msg;

			ByteBuf content = httpContent.content();

			if (msg instanceof LastHttpContent) {
				LastHttpContent trailer = (LastHttpContent) msg;
				// readPostData();
			}
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

}