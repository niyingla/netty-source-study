package com.imooc.netty.ch6;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.util.concurrent.TimeUnit;

/**
 *  ChannelHandlerContext 就是pipeline中handler的某一个（当前）handler的内容
 * @author
 */
public class OutBoundHandlerB extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("OutBoundHandlerB: " + msg);
        //节点 往上传播事件
        ctx.write(msg, promise);
    }


    /**
     *  传播顺序  相反于添加顺序
     * @param ctx
     */
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) {
        //定时器调用
        ctx.executor().schedule(() -> {
            //对chanel写数据 pipeline.write tail 开始往传播父类的写事件（链）
            ctx.channel().write("hello world");
            ctx.write("hello world");
        }, 3, TimeUnit.SECONDS);
    }
}
