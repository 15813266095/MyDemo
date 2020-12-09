package com.zkw.springboot;

import com.zkw.springboot.bean.User;
import com.zkw.springboot.netty.Client;
import com.zkw.springboot.protocal.Message;
import com.zkw.springboot.protocal.MessageType;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@SpringBootApplication
public class DemoClientApplication implements CommandLineRunner {
    @Autowired
    private Client client;

    public static void main(String[] args) {
        SpringApplication.run(DemoClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Channel channel = null;
        ChannelFuture channelFuture = null;

        Message message = new Message();
        User user = new User();
        message.setMessageType(MessageType.LOGIN);
        user.setAccount("123");
        user.setPassword("123");
        user.setUsername("zkw");
        user.setScenes("地图1");
        user.setPosition_X(0);
        user.setPosition_Y(0);
        message.setUser(user);

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                client.destroy();
            }
        });

        while (true) {
            //向服务端发送内容
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String input = reader.readLine();
            if (input != null) {
                if("connect".equals(input)&&channel==null){
                    channelFuture = client.start("localhost", 8088);
                    channel=channelFuture.channel();
                }
                else if ("quit".equals(input)) {
                    channelFuture=null;
                    channel=null;
                    //client.destroy();
                    System.out.println("断开连接");
                    //System.exit(1);
                }
                else if(channel !=null){
                    channel.writeAndFlush(message);
                    System.out.println("客户端已发送信息");
                }
            }
        }

    }
}
