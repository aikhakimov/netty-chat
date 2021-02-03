package com.nettychat.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import jdk.nashorn.internal.codegen.CompilerConstants;

public class ClientHandler extends SimpleChannelInboundHandler<String> {

    private Callback msgCallback;

    public ClientHandler(Callback msgCallback) {
        this.msgCallback = msgCallback;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        if (msgCallback != null){
            msgCallback.callback(s);
        }
    }
}
