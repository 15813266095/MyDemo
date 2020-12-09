package com.zkw.springboot;

import com.zkw.springboot.netty.Client;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoClientApplication implements CommandLineRunner {
    @Autowired
    private Client client;

    public static void main(String[] args) {
        SpringApplication.run(DemoClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ChannelFuture channelFuture = client.start("localhost", 8080);
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                client.destroy();
            }
        });
        channelFuture.channel().closeFuture();
    }
}
