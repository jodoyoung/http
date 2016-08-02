package kr.co.anajo.http.handler;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import kr.co.anajo.context.ApplicationContext;
import kr.co.anajo.http.ResponseHelper;

public class DispatcherHandler extends SimpleChannelInboundHandler<FullHttpMessage> {

	private ResponseHelper responseHelper = ApplicationContext.getInstance().getBean(ResponseHelper.class);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpMessage msg) throws Exception {
		if (!msg.decoderResult().isSuccess()) {
			responseHelper.sendError(ctx, BAD_REQUEST);
			return;
		}

		if (msg instanceof HttpRequest) {
			HttpRequest request = (HttpRequest) msg;
			final String uri = request.uri();

			PathMatcher matcher = ApplicationContext.getInstance().getBean(PathMatcher.class);
			if (matcher.isStaticUri(uri)) {
				// TODO static controller
			}
			if (!matcher.isAuthenticationIgnoreUri(uri)) {
				// TODO authentication check
			}
			// TODO api(page & ajax) controller
		}

		if (msg instanceof HttpContent) {
			if (msg instanceof LastHttpContent) {

			}
		}

	}

}
