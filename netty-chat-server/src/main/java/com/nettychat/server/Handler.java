package com.nettychat.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

public class Handler extends SimpleChannelInboundHandler<String> {

    private static final List<Channel> channels = new ArrayList<>();
    private String name;
    private static int number = 1;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Connected:" + ctx);
        channels.add(ctx.channel());
        name = "Client" + number;
        number++;
        String out = "[Сервер]: " + name + " подключился.\n";
        broadcast(out);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        if (s.startsWith("/changename")){

            String newname = s.split("\\s", 2)[1];
            String out = String.format("[Сервер]: %s меняет имя на %s.\n", name, newname);
            this.name = newname;
            broadcast(out);
            return;
        }
        String out = String.format("[%s]: %s\n", name, s);
        broadcast(out);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        channels.remove(ctx.channel());
        String out = "[Сервер]: " + name + " отключился.\n";
        broadcast(out);
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        channels.remove(ctx.channel());
        String out = "[Сервер]: " + name + " отключился.\n";
        broadcast(out);
        ctx.close();
    }

    private void broadcast(String out){
        for (Channel c : channels){
            c.writeAndFlush(out);
        }
    }
}
