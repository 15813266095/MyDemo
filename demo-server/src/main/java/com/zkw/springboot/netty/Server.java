package com.zkw.springboot.netty;

import com.zkw.springboot.facade.MessageHandlerManager;
import com.zkw.springboot.service.HeartbeatService;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangkewei
 * @date 2020/12/16 15:31
 * @desc 服务器的启动和关闭
 */
@Slf4j
@Component
public class Server {
    @Autowired
    private HeartbeatService heartbeatService;
    @Autowired
    private MessageHandlerManager messageHandlerManager;

    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private Channel channel;

    @Value("${demoServer.readerIdleTime}")
    long readerIdleTime;
    @Value("${demoServer.writerIdleTime}")
    long writerIdleTime;
    @Value("${demoServer.allIdleTime}")
    long allIdleTime;

    /**
     * 配置服务器信息，开启服务器
     * @param hostname
     * @param port
     * @return
     */
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
                        protected void initChannel(SocketChannel socketChannel){
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new ObjectDecoder(1024*1024,ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(new IdleStateHandler(readerIdleTime,writerIdleTime,allIdleTime,TimeUnit.SECONDS));
                            pipeline.addLast(new ServerHandler(heartbeatService, messageHandlerManager));
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

    /**
     * 关闭服务器
     */
    public void destroy(){
        if(channel != null) {
            channel.close();
        }
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        log.info("关闭服务端完成");
    }
}
