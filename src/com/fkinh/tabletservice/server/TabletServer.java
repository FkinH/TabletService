package com.fkinh.tabletservice.server;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Author: jinghao
 * Date: 2016-03-14.
 */
public class TabletServer {

    public static final int PORT = 2333;

    public static TabletServer server;

    public synchronized static TabletServer getInstance(){
        if(server == null){
            server = new TabletServer();
        }
        return server;
    }

    public void start() throws Exception {
        Selector selector = null;
        ServerSocketChannel server = null;
        try {
            selector = Selector.open();
            server = ServerSocketChannel.open();
            server.socket().bind(new InetSocketAddress(PORT));
            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                selector.select();
                for (Iterator<SelectionKey> i = selector.selectedKeys().iterator(); i.hasNext(); ) {
                    SelectionKey key = i.next();
                    i.remove();
                    if (!key.isValid()) {
                        continue;
                    }
                    if (key.isAcceptable()) {
                        System.out.println("acceptable");
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        System.out.println("Server: accept client socket " + socketChannel);
                        socketChannel.configureBlocking(false);
                        socketChannel.register(key.selector(), SelectionKey.OP_READ);
                    } else if (key.isWritable()) {
                        System.out.println("writable");
                        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
                        byteBuffer.flip();
                        SocketChannel socketChannel = (SocketChannel)key.channel();
                        socketChannel.write(byteBuffer);
                        if(byteBuffer.hasRemaining()) {
                            key.interestOps(SelectionKey.OP_READ);
                        }
                        byteBuffer.compact();
                    } else if (key.isReadable()) {
                        System.out.println("readable");
                        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
                        SocketChannel socketChannel = (SocketChannel)key.channel();
                        while(true) {
                            int readBytes = socketChannel.read(byteBuffer);
                            if(readBytes>0) {
                                System.out.println("Server: readBytes = " + readBytes);
                                System.out.println("Server: data = " + new String(byteBuffer.array(), 0, readBytes));
                                byteBuffer.flip();
                                socketChannel.write(byteBuffer);
                                break;
                            }
                        }
                        socketChannel.close();
                    }
                }
            }
        } catch (Throwable e) {
            throw new RuntimeException("Server failure: " + e.getMessage());
        } finally {
            try {
                assert selector != null;
                selector.close();
                assert server != null;
                server.socket().close();
                server.close();
            } catch (Exception e) {
                // do nothing - server failed
            }
        }
    }

    public static void main(String[] args){
        TabletServer server = TabletServer.getInstance();
        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

