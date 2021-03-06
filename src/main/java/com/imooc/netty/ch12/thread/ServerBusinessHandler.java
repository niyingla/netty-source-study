package com.imooc.netty.ch12.thread;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @Sharable annotation 表明Handler可以在不同Channel之间共享。
 * 只要加上@ChannelHandler.Sharable注解，他在整个生命周期中就是以单例的形式存在了
 */
@ChannelHandler.Sharable
public class ServerBusinessHandler extends SimpleChannelInboundHandler<ByteBuf> {
    public static final ChannelHandler INSTANCE = new ServerBusinessHandler();


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        //获取内存
        ByteBuf data = Unpooled.directBuffer();
        //获取请求内容
        data.writeBytes(msg);
        Object result = getResult(data);
        //写回原结果
        ctx.channel().writeAndFlush(result);
    }

    /**
     * 假设为接收数据后的处理逻辑
     * @param data
     * @return
     */
    protected Object getResult(ByteBuf data) {
        // 90.0% == 1ms
        // 95.0% == 10ms  1000 50 > 10ms
        // 99.0% == 100ms 1000 10 > 100ms
        // 99.9% == 1000ms1000 1 > 1000ms
        int level = ThreadLocalRandom.current().nextInt(1, 1000);

        int time;
        if (level <= 900) {
            time = 1;
        } else if (level <= 950) {
            time = 10;
        } else if (level <= 990) {
            time = 100;
        } else {
            time = 1000;
        }

        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }

        return data;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // ignore
    }
}
