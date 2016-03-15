package com.fkinh.tabletservice.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Author: jinghao
 * Date: 2016-03-14.
 */
public class TabletClient {

    private InetSocketAddress inetSocketAddress;
    private SocketChannel socketChannel;

    public TabletClient(String hostname, int port) {
        inetSocketAddress = new InetSocketAddress(hostname, port);
    }

    public void send(String requestData) {
        try {
            socketChannel = SocketChannel.open(inetSocketAddress);
            socketChannel.configureBlocking(false);
            socketChannel.write(ByteBuffer.wrap(requestData.getBytes()));
//            ByteBuffer byteBuffer = ByteBuffer.allocate(512);
//            while (true) {
//                byteBuffer.clear();
//                int readBytes = socketChannel.read(byteBuffer);
//                if (readBytes > 0) {
//                    byteBuffer.flip();
//                    System.out.println("Client: readBytes = " + readBytes);
//                    System.out.println("Client: data = " + new String(byteBuffer.array(), 0, readBytes));
//                    break;
//                }
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void release(){
        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int a = 1;
        String hostname = "localhost";
        String requestData = "Actions speak louder than words!";
        int port = 2333;
        TabletClient client = new TabletClient(hostname, port);
        while (true){
            client.send(requestData);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        client.release();
    }

}
