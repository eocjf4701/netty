// modified from http://netty.io/wiki/user-guide-for-4.x.html
package nettystartup.h1.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;

// 여러 채널에서 동시에 사용될 수 있는 핸들러
@ChannelHandler.Sharable
// 수신된 메시지에 대한 처리를 담당하는 핸들러
class DiscardServerHandler extends ChannelInboundHandlerAdapter {
    // 클라이언트로부터 데이터를 수신
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        try {
            // discard
            System.out.println("수신성공");
        } finally {
            // 메모리 반환
            buf.release();
        }
    }

    // 핸들러에서 예외가 발생할 때 호출
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
