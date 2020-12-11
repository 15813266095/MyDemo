package com.zkw.springboot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class DemoClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoClientApplication.class, args);
    }

//    @Override
//    public void run(String... args) throws Exception {
//        Channel channel = null;
//        ChannelFuture channelFuture = null;
//
//        Message message = new Message();
//        User user = new User();
//        message.setMessageType(MessageType.GET);
//        message.setDirection("right");
//        user.setAccount("123");
//        user.setPassword("123");
//        user.setUsername("zkw");
//        user.setScenes("地图1");
//        user.setPosition_X(0);
//        user.setPosition_Y(0);
//        message.setUser(user);
//
//        Runtime.getRuntime().addShutdownHook(new Thread(){
//            @Override
//            public void run() {
//                client.destroy();
//            }
//        });
//
//        while (true) {
//            //向服务端发送内容
//            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//            String input = reader.readLine();
//            if (input != null) {
//                if("connect".equals(input)&&(channel==null||!channel.isActive())){
//                    channelFuture = client.start("localhost", 8881);
//                    channel=channelFuture.channel();
//                }
//                else if ("quit".equals(input)) {
//                    if(channel!=null){
//                        Message request = new Message();
//                        request.setMessageType(MessageType.DISCONNECT);
//                        channel.writeAndFlush(request);
//                        channel.close();
//                    }
//                    channel=null;
//                    //client.destroy();
//                    log.info("断开连接");
//                    //System.exit(1);
//                }
//                else if(channel!=null&&channel.isActive()){
//                    channel.writeAndFlush(message);
//                    log.info("客户端已发送消息体");
//                }
//            }
//        }
//
//    }
}
