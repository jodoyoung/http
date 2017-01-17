package kr.co.anajo.http.handler;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import kr.co.anajo.component.auth.Session;
import kr.co.anajo.component.auth.SessionManager;
import kr.co.anajo.context.ApplicationContext;
import kr.co.anajo.http.ResponseHelper;

public class AuthenticationHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private Logger logger = LoggerFactory.getLogger(AuthenticationHandler.class);

	private SessionManager sessionManager = ApplicationContext.getInstance().getBean(SessionManager.class);

	private ResponseHelper responseHelper = ApplicationContext.getInstance().getBean(ResponseHelper.class);

	private URLMatcher urlMatcher = ApplicationContext.getInstance().getBean(URLMatcher.class);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		if (!request.decoderResult().isSuccess()) {
			responseHelper.sendError(ctx, BAD_REQUEST);
			return;
		}

		final String uri = request.uri();

		if (!urlMatcher.isAuthenticationIgnoreUri(uri)) {
			String sessionId = request.headers().get(SessionManager.SESSiON_COOKIE_NAME);
			if (sessionId == null) {
				responseHelper.sendRedirect(ctx, "/auth/login");
				return;
			}
			Session session = sessionManager.get(sessionId);
			if (session == null) {
				logger.warn("session is null. session id: {}", sessionId);
				responseHelper.sendRedirect(ctx, "/auth/login");
				return;
			}
			InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
			String clientIp = socketAddress.getAddress().getHostAddress();
			if (!session.isValid(clientIp)) {
				logger.warn("session is invalid. client ip: {}, session: {}", clientIp, session);
				responseHelper.sendRedirect(ctx, "/auth/login");
				return;
			}
		}
		ctx.fireChannelRead(request);
	}

}