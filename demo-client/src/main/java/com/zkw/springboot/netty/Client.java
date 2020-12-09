package com.zkw.springboot.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
public class Client {
    NioEventLoopGroup group = new NioEventLoopGroup();
    Channel channel = null;
    public ChannelFuture start(String host,int port){
        ChannelFuture channelFuture = null;
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(new ClientHandler());
                        }
                    });

            System.out.println("正在建立客户端连接");
            channelFuture = bootstrap.connect().sync();
            channel=channelFuture.channel();
            System.out.println("建立连接成功");


        }catch (Exception e){
            e.printStackTrace();
        }
        return channelFuture;
    }

    public void destroy(){
        if(channel != null) {
            channel.close();
            channel=null;
        }
        group.shutdownGracefully();
        System.out.println("客户端成功关闭");
    }
}
