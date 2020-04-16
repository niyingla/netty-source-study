package com.imooc.netty.ch2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;

    public Server(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            System.out.println("服务端启动成功，端口:" + port);
        } catch (IOException exception) {
            System.out.println("服务端启动失败");
        }
    }

    public void start() {
        new Thread(() -> doStart()).start();
    }

    private void doStart() {
        //while 等于nioEventLoop run方法
        while (true) {
            //《手记》 nioEventLoop 分为两种
            // 1  boss 一个 专门负责发现客户端连接
            // 2 worker 有限的若干个，默认的个数只是为 CPU 核数的 2 倍，它负责处理客户端连接的信息读写
            try {
                //对应nio润方法 this.select 用于处理连接
                Socket client = serverSocket.accept();
                new ClientHandler(client).start();
            } catch (IOException e) {
                System.out.println("服务端异常");
            }
        }
    }
}
