package com.zkw.springboot;

import com.zkw.springboot.netty.Server;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoServerApplication implements CommandLineRunner {
    @Autowired
    private Server server;
    @Value("${demoServer.hostname}")
    private String hostname;
    @Value("${demoServer.port}")
    private int port;

    public static void main(String[] args) {
        SpringApplication.run(DemoServerApplication.class, args);
    }

    /**
     * 启动服务器
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        ChannelFuture future = server.start(hostname,port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> server.destroy()));
        future.channel().closeFuture().sync();
    }

}
