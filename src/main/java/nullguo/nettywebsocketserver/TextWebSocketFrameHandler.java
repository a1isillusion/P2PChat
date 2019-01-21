package nullguo.nettywebsocketserver;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import nullguo.p2pclient.P2PClient;
import nullguo.p2pserver.P2PServerHandler;
import nullguo.pojo.Request;

public class TextWebSocketFrameHandler extends
SimpleChannelInboundHandler<TextWebSocketFrame> {

public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

@Override
protected void channelRead0(ChannelHandlerContext ctx,
    TextWebSocketFrame msg) throws Exception { 
Channel incoming = (Channel) ctx.channel();
for (io.netty.channel.Channel channel : channels) {
    if (channel != incoming){
        channel.writeAndFlush(new TextWebSocketFrame("[" + incoming.remoteAddress() + "]" + msg.text()));
    } else {
        channel.writeAndFlush(new TextWebSocketFrame("[you]" + msg.text() ));
        Request request=new Request();
        request.setAddress(incoming.remoteAddress().toString());
        request.setType(2);
        request.setData("["+P2PServerHandler.localAddress+"]" + msg.text());
        for(String address:P2PServerHandler.addressList) {
        	P2PClient.send(address, request);
        }
    }
}
}

@Override
public void handlerAdded(ChannelHandlerContext ctx) throws Exception {  
Channel incoming = ctx.channel();
for (Channel channel : channels) {
    channel.writeAndFlush(new TextWebSocketFrame("[浏览器] - " + incoming.remoteAddress() + " 加入"));
}
channels.add(ctx.channel());
Request request=new Request();
request.setAddress(incoming.remoteAddress().toString());
request.setType(2);
request.setData("[浏览器] - " + incoming.remoteAddress() + " 加入");
for(String address:P2PServerHandler.addressList) {
	P2PClient.send(address, request);
}
System.out.println("浏览器:"+incoming.remoteAddress() +"加入");
}

@Override
public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  
Channel incoming = ctx.channel();
for (Channel channel : channels) {
    channel.writeAndFlush(new TextWebSocketFrame("[浏览器] - " + incoming.remoteAddress() + " 离开"));
}
channels.remove(ctx.channel());
Request request=new Request();
request.setAddress(incoming.remoteAddress().toString());
request.setType(2);
request.setData("[浏览器] - " + incoming.remoteAddress() + " 离开");
for(String address:P2PServerHandler.addressList) {
	P2PClient.send(address, request);
}
System.out.println("浏览器:"+incoming.remoteAddress() +"离开");
}

@Override
public void channelActive(ChannelHandlerContext ctx) throws Exception { 
Channel incoming = ctx.channel();
System.out.println("浏览器:"+incoming.remoteAddress()+"在线");
}

@Override
public void channelInactive(ChannelHandlerContext ctx) throws Exception { 
Channel incoming = ctx.channel();
System.out.println("浏览器:"+incoming.remoteAddress()+"掉线");
}

@Override
public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    throws Exception {
Channel incoming = ctx.channel();
System.out.println("浏览器:"+incoming.remoteAddress()+"异常");
// 当出现异常就关闭连接
cause.printStackTrace();
ctx.close();
}

}
