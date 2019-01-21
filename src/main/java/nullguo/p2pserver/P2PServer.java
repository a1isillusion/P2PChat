package nullguo.p2pserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import nullguo.nettywebsocketserver.WebsocketChatServerInitializer;
import nullguo.p2pclient.P2PClient;
import nullguo.pojo.Request;

public class P2PServer {
	public static void initP2PServer(int WebPort,int P2PPort,String localAddress) {
		P2PServerHandler.localAddress=localAddress;
	    NioEventLoopGroup bosseventLoopGroup = new NioEventLoopGroup();
	    NioEventLoopGroup childeventLoopGroup = new NioEventLoopGroup();
		try {
	        ServerBootstrap P2PBootstrap = new ServerBootstrap();
	        P2PBootstrap.group(bosseventLoopGroup,childeventLoopGroup)
	            .channel(NioServerSocketChannel.class)
	            .childHandler(new ChannelInitializer<SocketChannel>() {
	              @Override
	              protected void initChannel(SocketChannel ch) throws Exception {
	                ChannelPipeline p = ch.pipeline();
	                p.addLast(new StringDecoder());
	                p.addLast(new StringEncoder());
	                p.addLast(new P2PServerHandler());
	              }
	            });
	        ServerBootstrap WebBootstrap = new ServerBootstrap(); 
	        WebBootstrap.group(bosseventLoopGroup, childeventLoopGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(new WebsocketChatServerInitializer()) 
             .option(ChannelOption.SO_BACKLOG, 128)
             .childOption(ChannelOption.SO_KEEPALIVE, true);
 
            System.out.println("WebsocketChatServer 启动了");
            ChannelFuture f1=P2PBootstrap.bind(P2PPort);
            ChannelFuture f2=WebBootstrap.bind(WebPort);
            f1.channel().closeFuture().sync();
            f2.channel().closeFuture().sync();
	      } catch (InterruptedException e) {
	        e.printStackTrace();
	      } finally {  
	          // Shut down the event loop to terminate all threads.  
	    	  bosseventLoopGroup.shutdownGracefully();  
	    	  childeventLoopGroup.shutdownGracefully();
	      }  
	    }
	public static void initP2PServer(int WebPort,int P2PPort,String localAddress,String remoteAddress) {
		P2PServerHandler.localAddress=localAddress;
		Request request=new Request();
		request.setType(1);
		request.setAddress(localAddress);
		P2PClient.send(remoteAddress, request);
	    NioEventLoopGroup bosseventLoopGroup = new NioEventLoopGroup();
	    NioEventLoopGroup childeventLoopGroup = new NioEventLoopGroup();
		try {
	        ServerBootstrap P2PBootstrap = new ServerBootstrap();
	        P2PBootstrap.group(bosseventLoopGroup,childeventLoopGroup)
	            .channel(NioServerSocketChannel.class)
	            .childHandler(new ChannelInitializer<SocketChannel>() {
	              @Override
	              protected void initChannel(SocketChannel ch) throws Exception {
	                ChannelPipeline p = ch.pipeline();
	                p.addLast(new StringDecoder());
	                p.addLast(new StringEncoder());
	                p.addLast(new P2PServerHandler());
	              }
	            });
	        ServerBootstrap WebBootstrap = new ServerBootstrap(); 
	        WebBootstrap.group(bosseventLoopGroup, childeventLoopGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(new WebsocketChatServerInitializer()) 
             .option(ChannelOption.SO_BACKLOG, 128)
             .childOption(ChannelOption.SO_KEEPALIVE, true);
 
            System.out.println("WebsocketChatServer 启动了");
            ChannelFuture f1=P2PBootstrap.bind(P2PPort);
            ChannelFuture f2=WebBootstrap.bind(WebPort);
            f1.channel().closeFuture().sync();
            f2.channel().closeFuture().sync();
	      } catch (InterruptedException e) {
	        e.printStackTrace();
	      } finally {  
	          // Shut down the event loop to terminate all threads.  
	    	  bosseventLoopGroup.shutdownGracefully();  
	    	  childeventLoopGroup.shutdownGracefully();
	      }  
	    }
}
