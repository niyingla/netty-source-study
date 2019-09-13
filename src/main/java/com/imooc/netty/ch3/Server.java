package com.imooc.netty.ch3;

import com.imooc.netty.ch6.AuthHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;

/**
 * @author
 */
public final class Server {

    public static void main(String[] args) throws Exception {
        //介入管理  默认创建两倍cpu
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //读写管理
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            //设置基本线程
            b.group(bossGroup, workerGroup)
                    //设置服务的ServerSocketChannel类型  后面会通过反射创建实例
                    .channel(NioServerSocketChannel.class)
                    //设置客户端连接属性
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    //创建连接时绑定基本属性
                    .childAttr(AttributeKey.newInstance("childAttr"), "childAttrValue")
                    //服务端启动需要的处理逻辑
                    .handler(new ServerHandler())
                    //连接逻辑处理
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            //Pipeline 逻辑链 每次数据读写都会经过这个逻辑链
                            ch.pipeline().addLast(new AuthHandler());
                            //..

                        }
                    });
            //端口绑定并启动
            ChannelFuture f = b.bind(8888).sync();

            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}