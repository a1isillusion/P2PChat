package nullguo.p2pserver;

import java.util.HashSet;
import java.util.Set;

import com.alibaba.fastjson.JSON;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import nullguo.nettywebsocketserver.TextWebSocketFrameHandler;
import nullguo.p2pclient.P2PClient;
import nullguo.pojo.Request;
import nullguo.pojo.Response;

public class P2PServerHandler extends ChannelInboundHandlerAdapter {
	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	public static Set<String> addressList=new HashSet<String>();
	public static String localAddress;
	  @Override
	  public void channelRead(ChannelHandlerContext ctx, Object msg) {
		 Request request=JSON.parseObject((String)msg, Request.class);
         System.out.println(JSON.toJSONString(request));
         if(request.getType()==1) {
        	 addressList.add(request.getAddress());
        	 Response response=new Response();
        	 response.setType(1);
        	 Set<String> allAddressList=new HashSet<String>();
        	 allAddressList.addAll(addressList);
        	 allAddressList.add(localAddress);
        	 response.setAddressList(allAddressList);
        	 ctx.writeAndFlush(JSON.toJSONString(response));
        	 request.setType(3);
        	 for(String address:addressList) {
        		 P2PClient.send(address, request);
        	 }
         }
         else if(request.getType()==2) {
        	 Response response=new Response();
        	 response.setType(2);
        	 ctx.writeAndFlush(JSON.toJSONString(response));
        	 for (Channel channel : TextWebSocketFrameHandler.channels) {
        	  channel.writeAndFlush(new TextWebSocketFrame("[" + ctx.channel().remoteAddress() + "]" + request.getData()));
        	}
		}
         else if(request.getType()==3) {
        	 addressList.add(request.address);
        	 addressList.remove(localAddress);
        	 Response response=new Response();
        	 response.setType(3);
        	 ctx.writeAndFlush(JSON.toJSONString(response));
         }
	  }
	}
