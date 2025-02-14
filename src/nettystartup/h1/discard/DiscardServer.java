// modified from io.netty.example.discard
package nettystartup.h1.discard;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

final class DiscardServer {
    public static void main(String[] args) throws Exception {
        // 비동기 네트워크 처리를 위해 여러 개의 스레드를 관리하는 역할
        // 클라이언트의 연결을 수락하는 역할(연결을 수락하고 workerGroup으로 넘겨주는 일을 합니다.)
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // CPU 코어 수만큼의 스레드를 생성하여 workerGroup을 구성합니다. 이 방식은 서버가 더 많은 동시 연결을 처리할 수 있도록 스레드를 자동으로 분배
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 서버의 설정을 구성하고, 실제 서버를 시작하는 작업
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    // 서버에서 사용할 채널(Channel) 타입을 설정
                    // 클라이언트의 연결을 비동기적으로 수락
                    .channel(NioServerSocketChannel.class)
                    // Netty에서 네트워크 이벤트를 로깅할 수 있도록 해주는 핸들러
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // childHandler는 클라이언트 연결에 대한 핸들러를 설정하는 메서드
                    .childHandler(new DiscardServerHandler());
            // 서버 소켓을 지정된 포트에 바인딩하고, 클라이언트의 연결을 기다리는 상태
            ChannelFuture f = b.bind(8010).sync();
            System.err.println("Ready for 0.0.0.0:8010");
            // 서버 채널이 닫힐 때까지 기다리는 작업을 수행
            f.channel().closeFuture().sync();

            EmbeddedChannel ch =
                    new EmbeddedChannel(null);
        } finally {
            // 그룹 내의 모든 이벤트 루프 스레드를 종료하고, 모든 연결이 정상적으로 종료될 때까지 대기
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
