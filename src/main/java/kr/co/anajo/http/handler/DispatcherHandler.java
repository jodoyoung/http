package kr.co.anajo.http.handler;

import java.util.logging.Logger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import kr.co.anajo.context.ApplicationContext;
import kr.co.anajo.http.ResponseHelper;

public class DispatcherHandler extends ChannelInboundHandlerAdapter {

	private final Logger logger = Logger.getLogger(DispatcherHandler.class.getName());

	private ResponseHelper responseHelper = ApplicationContext.getInstance().getBean(ResponseHelper.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		if (msg instanceof HttpRequest) {
			FullHttpRequest request = (FullHttpRequest) msg;
			final String uri = request.uri();
			logger.info(() -> String.format("request uri: %s", uri));

			PathMatcher matcher = ApplicationContext.getInstance().getBean(PathMatcher.class);
			if (matcher.isStaticUri(uri)) {
				ApplicationContext.getInstance().getBean(StaticResourceHandler.class).handle(ctx, request);
				return;
			}
			// if (!matcher.isAuthenticationIgnoreUri(uri)) {
			// // TODO authentication check
			// responseHelper.sendRedirect(ctx, "/login");
			// }

			// TODO api(page & ajax) controller
			ApplicationContext.getInstance().getBean(ApiHandler.class).handle(ctx, request);
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
