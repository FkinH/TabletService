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

    public static final String HOST = "localhost";

    public static final int PORT = 2333;

    public static TabletClient client;

    private InetSocketAddress inetSocketAddress;

    private SocketChannel socketChannel;

    public synchronized static TabletClient getInstance(){
        if(client == null){
            client = new TabletClient(HOST, PORT);
        }
        return client;
    }

    private TabletClient(String hostname, int port) {
        inetSocketAddress = new InetSocketAddress(hostname, port);
    }

    public void send(String requestData) {
        try {
            socketChannel = SocketChannel.open(inetSocketAddress);
            socketChannel.configureBlocking(false);
            socketChannel.write(ByteBuffer.wrap(requestData.getBytes()));
            //read response
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
        String requestData = "Actions speak louder than words!";
        TabletClient client = new TabletClient(HOST, PORT);
        while (true){
            client.send(requestData);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        client.release();
    }

}
