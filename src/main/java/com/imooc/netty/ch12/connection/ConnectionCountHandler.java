package com.imooc.netty.ch12.connection;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Sharable
public class ConnectionCountHandler extends ChannelInboundHandlerAdapter {

    private AtomicInteger nConnection = new AtomicInteger();

    public ConnectionCountHandler() {


        //每两秒计算一次连接数
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() ->
                System.out.println("connections: " + nConnection.get()),
                0, 2, TimeUnit.SECONDS);

    }

    /**
     * 创建一个连接  +1
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        nConnection.incrementAndGet();
    }

    /**
     * 消除一个连接  -1
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        nConnection.decrementAndGet();
    }

}


