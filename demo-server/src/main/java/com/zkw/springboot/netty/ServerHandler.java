package com.zkw.springboot.netty;

import com.zkw.springboot.bean.User;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<Message> {

    int readIdleTimes = 0;//读空闲的计数

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //cause.printStackTrace();
        log.info("客户端关闭，断开连接");
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message request) throws Exception {
        log.info("收到客户端消息，消息类型为"+request.getMessageType());
        if(request.isActive()) {
            User user = request.getUser();
            Message response = new Message(true);
            response.setMessageType(MessageType.SUCCESS);
            switch (request.getMessageType()){
                //获取角色信息
                case GET:
                    response.setDescription("\n角色名为：" +user.getUsername()+
                            "\n当前角色位置为："+user.getArea()+
                            "\n当前角色地图在："+user.getScenes());
                    ctx.writeAndFlush(response);
                    break;

                //角色移动
                case MOVE:
                    user.move(request.getDirection());
                    response.setDescription("\n角色移动了，方向为"+request.getDirection()+ "\n当前角色位置为："+user.getArea());
                    ctx.writeAndFlush(response);
                    break;

                //角色登录
                case LOGIN:

                    break;

                //角色注册
                case REGISTER:

                    break;

                //角色场景切换
                case CHANGE_SCENES:
                    user.setScenes("场景2");
                    response.setDescription("当前角色场景为："+user.getScenes());
                    ctx.writeAndFlush(response);
                    break;

                //请求错误
                default:
                    response.setMessageType(MessageType.ERROR);
                    response.setDescription("请求方式错误");
                    ctx.writeAndFlush(response);
            }
            readIdleTimes=0;//重置读空闲的计数
        }else {
            log.info("客户端断开连接");
            ctx.channel().close().sync();
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent)evt;
        switch (event.state()){
            case READER_IDLE:
                readIdleTimes ++; // 读空闲的计数加1
                log.info(ctx.channel().remoteAddress() + "读空闲，累计次数"+readIdleTimes);
                break;
            case WRITER_IDLE:
                // 不处理
                break;
            case ALL_IDLE:
                // 不处理
        }
        if(readIdleTimes >= 3){
            log.info("服务器读空闲超过3次，关闭连接");
            Message message = new Message();
            message.setActive(false);
            ctx.channel().writeAndFlush(message);
            ctx.channel().close().sync();
        }
    }
}
