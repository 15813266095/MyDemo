package com.zkw.springboot.netty;

import com.zkw.springboot.distribution.MessageHandlerManager;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * @author zhangkewei
 * @date 2020/12/22 16:40
 * @desc 客户端启动类
 */
@Slf4j
@Component
public class Client {
    @Autowired
    private MessageHandlerManager messageHandlerManager;
    private final NioEventLoopGroup group = new NioEventLoopGroup();
    private Channel channel = null;

    /**
     * 启动netty服务，连接到对应地址的服务器
     * @param host
     * @param port
     * @return
     */
    public ChannelFuture start(String host,int port){
        ChannelFuture channelFuture = null;
        try {
            Bootstrap bootstrap = new Bootstrap();
            ClientHandler clientHandler = new ClientHandler();
            clientHandler.setHandlerManager(messageHandlerManager);
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new ObjectDecoder(1024*1024, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(clientHandler);
                        }
                    });
            log.info("正在建立客户端连接");
            channelFuture = bootstrap.connect().sync();
            channel=channelFuture.channel();
            log.info("建立连接成功");
        }catch (Exception e){
            e.printStackTrace();
        }
        return channelFuture;
    }

    /**
     * 断开netty连接
     */
    public void destroy(){
        if(channel != null) {
            channel.close();
            channel=null;
        }
        group.shutdownGracefully();
        log.info("客户端成功关闭");
    }
}
