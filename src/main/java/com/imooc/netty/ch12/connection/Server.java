package com.imooc.netty.ch12.connection;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import static com.imooc.netty.ch12.connection.Constant.BEGIN_PORT;
import static com.imooc.netty.ch12.connection.Constant.N_PORT;

/**
 * @author 闪电侠
 */
public final class Server {

    public static void main(String[] args) {
        new Server().start(BEGIN_PORT, N_PORT);
    }

    /**
     * 限制 1
     * ulimit -n 最大打开文件数 （一般为文件 tcp连接也算）
     * 解除限制
     * /etc/security/limits.conf
     * * hard nofile 1000000 //真正限制数
     * * soft nofile 1000000 //警告限制数
     * 重启生效
     *
     * 限制 2
     * 全局文件句柄限制
     * cat /proc/sys/fs/file-max
     * vim /etc/sysctl.conf
     * 加入一行
     * fs.file-max=1000000
     * sysctl -p
     *
     * 开启服务
     * @param beginPort 开始启动端口
     * @param nPort 启动端口数
     */
    public void start(int beginPort, int nPort) {
        System.out.println("server starting....");

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        //一般来说，一个端口释放后会等待两分钟之后才能再被使用，SO_REUSEADDR是让端口释放后立即就可以被再次使用
        bootstrap.childOption(ChannelOption.SO_REUSEADDR, true);
        //连接处理handler
        bootstrap.childHandler(new ConnectionCountHandler());


        for (int i = 0; i < nPort; i++) {
            int port = beginPort + i;
            bootstrap.bind(port).addListener((ChannelFutureListener) future -> {
                System.out.println("bind success in port: " + port);
            });
        }
        System.out.println("server started!");
    }
}
