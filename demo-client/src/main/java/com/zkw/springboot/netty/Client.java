package com.zkw.springboot.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
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
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new ClientHandler());
                        }
                    });
            channelFuture = bootstrap.connect().sync();
            channel=channelFuture.channel();
            System.out.println("客户端建立连接并发送信息");
        }catch (Exception e){
            e.printStackTrace();
        }
        return channelFuture;
    }

    public void destroy(){
        if(channel != null) {
            channel.close();
        }
        group.shutdownGracefully();
        System.out.println("客户端成功关闭");
    }
}
