package com.littlesky.rpc.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class NettyClientHandler extends ChannelHandlerAdapter {
    private StringBuffer inMessage;

    private String outMessage;

    public NettyClientHandler(StringBuffer inMessage, String outMessage)
    {
        this.inMessage = inMessage;
        this.outMessage = outMessage;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf byteBuf = ctx.alloc().buffer(4*outMessage.length());
        byteBuf.writeBytes(outMessage.getBytes());
        ctx.write(byteBuf);
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        byte[] inByte = new byte[in.readableBytes()];

        in.readBytes(inByte);
        String result = new String(inByte);
        if("null".equals(result))
        {
            inMessage = null;
        }
        else
        {
            inMessage.append(result);
        }

        in.release();
    }
}
