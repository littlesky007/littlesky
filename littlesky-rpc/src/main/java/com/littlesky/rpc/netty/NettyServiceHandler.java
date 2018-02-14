package com.littlesky.rpc.netty;

import com.alibaba.fastjson.JSONObject;
import com.littlesky.common.ParamUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.util.StringUtils;

public class NettyServiceHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf read = (ByteBuf) msg;
        byte[] result = new byte[read.readableBytes()];
        read.readBytes(result);
        String param = new String(result);
        read.release();

        //解析参数,并反射调用
       String resultMes = ParamUtil.invokeMethod(param);

       if(StringUtils.isEmpty(resultMes))
       {
           resultMes="null";
       }


       //向客户端返回消息
        ByteBuf out = ctx.alloc().buffer(4*resultMes.length());
        out.writeBytes(resultMes.getBytes());
        ctx.writeAndFlush(out);
        ctx.close();
    }
}
