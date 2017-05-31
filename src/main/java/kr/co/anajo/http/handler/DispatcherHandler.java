package kr.co.anajo.http.handler;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.AsciiString;
import kr.co.anajo.context.ApplicationContext;
import kr.co.anajo.http.ResponseHelper;

public class DispatcherHandler extends ChannelInboundHandlerAdapter {

	private final Logger logger = LoggerFactory.getLogger(DispatcherHandler.class);

	private ResponseHelper responseHelper = ApplicationContext.getInstance().getBean(ResponseHelper.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		HttpRequest request = null;
		FullHttpResponse response = null;

		if (msg instanceof HttpRequest) {
			request = (HttpRequest) msg;

			if (request.decoderResult().isFailure()) {
				responseHelper.sendError(ctx, BAD_REQUEST);
				return;
			}

			if (HttpUtil.is100ContinueExpected(request)) {
				ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
			}

			String uri = request.uri();

			// controller replace
			ApplicationContext applicationContext = ApplicationContext.getInstance();
			String handleClassMethod = applicationContext.getUrlHandler(uri);

			if (handleClassMethod == null) {
				logger.info("not found request handler - uri: {}", uri);
				responseHelper.sendError(ctx, NOT_FOUND);
				return;
			}

			String[] handler = handleClassMethod.split("\\.");
			String handleClassName = handler[0];
			String handleMethodName = handler[1];

			Object controller = applicationContext.getBean(handleClassName);
			Class<?> klass = controller.getClass();
			Method handleMethod = klass.getDeclaredMethod(handleMethodName, null);
			logger.info("request uri: {}, handler: {}", uri, handleMethod);

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
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

}
