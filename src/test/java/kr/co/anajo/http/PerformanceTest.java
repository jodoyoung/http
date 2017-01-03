package kr.co.anajo.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

public class PerformanceTest {

	private static long count = 0;
	private static long startTime = 0;

	public static void main(String[] args) throws Exception {
//		oio();
		 netty();
	}

	private static void netty() throws Exception {
		EventLoopGroup group = new NioEventLoopGroup(10);
		EventLoopGroup workerGroup = new NioEventLoopGroup(500);
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(group, workerGroup).channel(NioServerSocketChannel.class).localAddress(new InetSocketAddress(8707))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
								@Override
								public void channelActive(ChannelHandlerContext ctx) throws Exception {
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									ctx.writeAndFlush(Unpooled.copiedBuffer("Hell World\n", CharsetUtil.UTF_8));
									count++;
									if (startTime == 0) {
										startTime = System.currentTimeMillis();
									}
									System.out.println(count + " : " + (System.currentTimeMillis() - startTime));
									ctx.close();
								}

								@Override
								protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
								}

								@Override
								public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
										throws Exception {
									cause.printStackTrace();
									ctx.close();
								}
							});
						}
					});
			ChannelFuture f = b.bind().sync();
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully().sync();
		}
	}

	private static void oio() throws Exception {
		final ServerSocket ss = new ServerSocket(8707);

		for (;;) {
			final Socket cs = ss.accept();
			new Thread(new Runnable() {
				@Override
				public void run() {
					OutputStream out;
					try {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						out = cs.getOutputStream();
						out.write("Hell World\n".getBytes(Charset.forName("UTF-8")));
						out.flush();
						cs.close();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							cs.close();
						} catch (IOException ioe) {
							// ignore
						}
					}
					count++;
					if (startTime == 0) {
						startTime = System.currentTimeMillis();
					}
					System.out.println(count + " : " + (System.currentTimeMillis() - startTime));
				}
			}).start();
		}
	}

}
