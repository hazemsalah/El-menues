 import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
 
public class ServerHandler extends ChannelInboundHandlerAdapter {
 
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    LoopBackTimeStamp ts = (LoopBackTimeStamp) msg;
    ts.setRecvTimeStamp(System.nanoTime());
//    ctx.write(msg); // (1)
//    ctx.flush(); // (2)
    ByteBuf content = Unpooled.copiedBuffer("Hello World.", CharsetUtil.UTF_8);
    FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
    response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain");
    response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, content.readableBytes());
    ctx.write(response);
    System.out.println("loop delay in ms : " + 1.0 * ts.timeLapseInNanoSecond() / 1000000L);
  }
 
  // Here is how we send out heart beat for idle to long
  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    if (evt instanceof IdleStateEvent) {
      IdleStateEvent event = (IdleStateEvent) evt;
      if (event.state() == IdleState.ALL_IDLE) { // idle for no read and write
        ctx.writeAndFlush(new LoopBackTimeStamp());
      }
    }
  }
 
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    // Close the connection when an exception is raised.
    cause.printStackTrace();
    ctx.close();
  }
}