package kr.co.anajo.http.handler;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.AsciiString;
import io.netty.util.CharsetUtil;
import kr.co.anajo.context.ApplicationContext;
import kr.co.anajo.context.annotation.Component;

@Component
public class ApiHandler {

	private final Logger logger = Logger.getLogger(ApiHandler.class.getName());

	protected void handle(ChannelHandlerContext ctx, FullHttpMessage msg) throws Exception {
		HttpRequest request = null;
		FullHttpResponse response = null;

		if (msg instanceof HttpRequest) {
			request = (HttpRequest) msg;

			if (request.decoderResult().isFailure()) {
				sendError(ctx, BAD_REQUEST);
				return;
			}

			if (HttpUtil.is100ContinueExpected(msg)) {
				ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
			}

			String uri = request.uri();

			// controller replace
			ApplicationContext applicationContext = ApplicationContext.getInstance();
			String handleClassMethod = applicationContext.getUrlHandler(uri);

			if (handleClassMethod == null) {
				logger.info(() -> String.format("not found request handler - uri: %s", uri));
				sendError(ctx, NOT_FOUND);
				return;
			}

			String[] handler = handleClassMethod.split("\\.");
			String handleClassName = handler[0];
			String handleMethodName = handler[1];

			Object controller = applicationContext.getBean(handleClassName);
			Class<?> klass = controller.getClass();
			Method handleMethod = klass.getDeclaredMethod(handleMethodName, null);
			logger.info(() -> String.format("request uri: %s, handler: %s", uri, handleMethod));

			response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer("11111111".getBytes()));
			response.headers().set(new AsciiString("Content-Type"), "text/plain");
			response.headers().set(new AsciiString("Content-Length"), response.content().readableBytes());

			if (HttpUtil.isKeepAlive(request)) {
				response.headers().set(new AsciiString("Connection"), new AsciiString("keep-alive"));
				ctx.write(response);
			} else {
				ctx.write(response).addListener(ChannelFutureListener.CLOSE);
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
		ctx.flush();
	}


	private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status,
				Unpooled.copiedBuffer("Failure: " + status + "\r\n", CharsetUtil.UTF_8));
		response.headers().set(new AsciiString("Content-Type"), "text/plain; charset=UTF-8");

		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

}