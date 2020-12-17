package com.zkw.springboot;

import com.zkw.springboot.dao.MapMapper;
import com.zkw.springboot.dao.UserMapper;
import com.zkw.springboot.netty.Server;
import io.netty.channel.ChannelFuture;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.zkw.springboot.dao")
@SpringBootApplication
public class DemoServerApplication implements CommandLineRunner {
    @Autowired
    Server server;
    @Autowired
    MapMapper mapMapper;
    @Autowired
    UserMapper userMapper;

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
        ChannelFuture future = server.start("localhost",8881);
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                server.destroy();
            }
        });
        future.channel().closeFuture().sync();
    }

}
