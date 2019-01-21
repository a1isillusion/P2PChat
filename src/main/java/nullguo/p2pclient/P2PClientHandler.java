package nullguo.p2pclient;

import com.alibaba.fastjson.JSON;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import nullguo.p2pserver.P2PServerHandler;
import nullguo.pojo.Response;

public class P2PClientHandler extends ChannelInboundHandlerAdapter {
	  @Override
	  public void channelRead(ChannelHandlerContext ctx, Object msg) {
	   System.out.println(JSON.toJSONString(msg));
	   Response response=JSON.parseObject((String)msg, Response.class);
	   if(response.getType()==1) {
		   P2PServerHandler.addressList=response.getAddressList();
		   P2PServerHandler.addressList.remove(P2PServerHandler.localAddress);
	   }else if(response.getType()==2) {
		   System.out.println("发送成功");
	   }else if(response.getType()==3) {
		   System.out.println("同步地址成功");
	   }
       ctx.close();
	  } 
	}
