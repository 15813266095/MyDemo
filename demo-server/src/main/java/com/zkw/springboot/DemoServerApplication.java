package com.zkw.springboot;

import com.zkw.springboot.netty.Server;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoServerApplication implements CommandLineRunner {
    @Autowired
    Server server;

    public static void main(String[] args) {
        SpringApplication.run(DemoServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ChannelFuture future = server.start("localhost",8088);
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                server.destroy();
            }
        });
        future.channel().closeFuture().sync();
    }

}
