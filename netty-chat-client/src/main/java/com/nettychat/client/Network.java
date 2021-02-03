package com.nettychat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Network {
    private SocketChannel sc;
    private Callback msgCallback;

    public Network(Callback msgCallback){
        Thread t = new Thread(() -> {
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try{
                Bootstrap b = new Bootstrap();
                b.group(workerGroup);
                b.channel(NioSocketChannel.class);
                b.handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        sc = socketChannel;
                        socketChannel.pipeline().addLast(new StringDecoder(), new StringEncoder(), new ClientHandler(msgCallback));
                    }
                });
                ChannelFuture f = b.connect("localhost", 8189).sync();
                f.channel().closeFuture().sync();
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public void send(String str){
        sc.writeAndFlush(str);
    }

    public void close(){
        sc.disconnect();
        sc.close();
    }
}
