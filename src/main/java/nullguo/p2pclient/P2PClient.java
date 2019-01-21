package nullguo.p2pclient;

import com.alibaba.fastjson.JSON;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import nullguo.pojo.Request;

public class P2PClient {
	public static Bootstrap b;
	public static void send(String address,Request request) {
	    EventLoopGroup group = new NioEventLoopGroup();
	    b = new Bootstrap();
	    b.group(group)
	        .channel(NioSocketChannel.class)
	        .handler(new ChannelInitializer<SocketChannel>() {
	          @Override
	          public void initChannel(SocketChannel ch) throws Exception {
	            ChannelPipeline p = ch.pipeline();
	            p.addLast(new StringDecoder());
	            p.addLast(new StringEncoder());
	            p.addLast(new P2PClientHandler());
	          }
	        });
	    try {	
	      Channel channel=b.connect(address.split(":")[0], Integer.parseInt(address.split(":")[1])).sync().channel();
	      channel.writeAndFlush(JSON.toJSONString(request));  
	      channel.closeFuture().sync();
	    } catch (InterruptedException e) {
	      e.printStackTrace();
	    }  finally {  
	        // Shut down the event loop to terminate all threads.  
	  	  group.shutdownGracefully();  
	    } 
	  }
}
