package kr.co.anajo.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpMessage;

public class DispatcherServlet extends SimpleChannelInboundHandler<FullHttpMessage> {

	@Override
	protected void channelRead0(ChannelHandlerContext arg0, FullHttpMessage arg1) throws Exception {
		// TODO Auto-generated method stub

	}

}
