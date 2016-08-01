package kr.co.anajo.http.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpMessage;

public class DispatcherHandler extends SimpleChannelInboundHandler<FullHttpMessage> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpMessage msg) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
