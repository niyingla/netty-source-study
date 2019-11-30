package com.imooc.netty.ch6;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * in说明事情开始   事件的触发 （大多数被动）
 * @author
 */
public class InBoundHandlerB extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("InBoundHandlerB: " + msg);
        //当前节点往下传播
        ctx.fireChannelRead(msg);
    }

    /**
     * 依据添加顺序传播
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //从头head结点 开始传播
        ctx.channel().pipeline().fireChannelRead("hello world");
    }
}
