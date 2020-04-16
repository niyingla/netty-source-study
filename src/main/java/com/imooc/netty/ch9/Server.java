package com.imooc.netty.ch9;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * direct 不受jvm管制的内存 需要手动回收
 * heap jvm内存 jvm管理个和gc
 * pooled 池化 unpooled 非池化
 * unsafe 依赖jdk unsafe对象
 * @author
 */
public final class Server {

    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                //解码器 二进制流-》byteBuf（自定义数据包）
                @Override
                public void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new Encoder());
                    ch.pipeline().addLast(new BizHandler());
                }
            });

            ChannelFuture f = b.bind(8888).sync();

            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
