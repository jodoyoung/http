package kr.co.anajo.http;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
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
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.AsciiString;
import kr.co.anajo.context.ApplicationContext;

public class DispatcherServlet extends SimpleChannelInboundHandler<FullHttpMessage> {

	private final Logger logger = Logger.getLogger(DispatcherServlet.class.getName());

	private static List<String> ignoreAuthenticationUri = new ArrayList<String>();
	static {
		ignoreAuthenticationUri.add("/auth");
		ignoreAuthenticationUri.add("/favicon.ico");
		ignoreAuthenticationUri.add("/static");
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpMessage msg) throws Exception {
		HttpRequest request = null;
		FullHttpResponse response = null;

		if (msg instanceof HttpRequest) {
			request = (HttpRequest) msg;

			if (HttpUtil.is100ContinueExpected(msg)) {
				ctx.write(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE));
			}

			HttpHeaders headers = request.headers();
			String uri = request.uri();
			HttpMethod method = request.method();
			System.out.println("HTTP: " + headers);
			System.out.println("HTTP: " + uri);
			System.out.println("HTTP: " + method);

			if (!ignoreAuthenticationUri.contains(uri)) {
				// TODO authentication (ex.login page, login proccess)
			}

			if ("/favicon.ico".equalsIgnoreCase(uri) || uri.startsWith("/static")) {
				// TODO favicon, static resource
			} else {
				// controller replace
				ApplicationContext applicationContext = ApplicationContext.getInstance();
				String handleClassMethod = applicationContext.getUrlHandler(uri);

				if (handleClassMethod == null) {
					logger.info(() -> String.format("not found request handler - uri: %s", uri));
				}

				String[] handler = handleClassMethod.split("\\.");
				String handleClassName = handler[0];
				String handleMethodName = handler[1];

				Object controller = applicationContext.getBean(handleClassName);
				Class<?> klass = controller.getClass();
				Method handleMethod = klass.getDeclaredMethod(handleMethodName, null);
				System.out.println(handleMethod);
			}

			response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
					Unpooled.wrappedBuffer("11111111".getBytes()));
			response.headers().set(new AsciiString("Content-Type"), "text/plain");
			response.headers().set(new AsciiString("Content-Length"), response.content().readableBytes());

			if (HttpUtil.isKeepAlive(request)) {
				ctx.write(response).addListener(ChannelFutureListener.CLOSE);
			} else {
				response.headers().set(new AsciiString("Connection"), new AsciiString("keep-alive"));
				ctx.write(response);
			}
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