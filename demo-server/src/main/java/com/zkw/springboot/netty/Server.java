package com.zkw.springboot.netty;

import com.zkw.springboot.handler.HandlerManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class Server {
    @Autowired
    private HandlerManager handlerManager;

    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private Channel channel;

    public ChannelFuture start(String hostname,int port){
        ChannelFuture channelFuture=null;
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .localAddress(new InetSocketAddress(hostname, port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new ObjectDecoder(1024*1024,ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(new IdleStateHandler(5,2,2, TimeUnit.SECONDS));
                            pipeline.addLast(new ServerHandler(handlerManager));
                        }
                    });
            channelFuture = serverBootstrap.bind().sync();
            channel=channelFuture.channel();
            log.info("开启服务端");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return channelFuture;
    }

    public void destroy(){
        if(channel != null) {
            channel.close();
        }
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        log.info("关闭服务端完成");
    }
}
