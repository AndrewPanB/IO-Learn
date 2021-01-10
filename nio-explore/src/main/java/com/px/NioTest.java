package com.px;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

public class NioTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        LinkedList<SocketChannel> list = new LinkedList<>();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(9090));
        serverSocketChannel.configureBlocking(false);

        while (true) {
            Thread.sleep(1000);
            SocketChannel socketChannel = serverSocketChannel.accept();
            if (socketChannel == null) {
                System.out.println("socketChannel = null");
            } else {
                socketChannel.configureBlocking(false);
                int port = socketChannel.socket().getPort();
                String ip = socketChannel.socket().getInetAddress().getHostAddress();
                System.out.println("Accept new SocketChannel, ip: " + ip + ", port: " + port);
                list.add(socketChannel);
            }
            ByteBuffer byteBuffer = ByteBuffer.allocate(10240);
            for (SocketChannel sc : list) {
                int num = sc.read(byteBuffer);
                if (num > 0) {
                    byteBuffer.flip();
                    byte[] bytes = new byte[byteBuffer.limit()];
                    byteBuffer.get(bytes);
                    String str = new String(bytes);
                    System.out.println(str);
                    byteBuffer.clear();
                } else {
                    String host = sc.socket().getInetAddress().getHostAddress();
                    int port = sc.socket().getPort();
                    System.out.println("Host: " + host + ", Port: " + port+", Buffer is NULL");
                }
            }
        }

    }
}
