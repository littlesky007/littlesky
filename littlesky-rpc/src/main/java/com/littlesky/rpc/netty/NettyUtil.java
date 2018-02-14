package com.littlesky.rpc.netty;

import com.littlesky.loadbalance.SelectedNode;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyUtil {
    public static  void startService(Integer port) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        try
        {
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyServiceHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,128);
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        }
        finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }

    public static StringBuffer invoke(String ip,Integer port,String message)
    {
        final StringBuffer result = new StringBuffer();
        try {
            EventLoopGroup workGroup = new NioEventLoopGroup();

            Bootstrap b = new Bootstrap();
            b.group(workGroup).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyClientHandler(result,message));
                        }
                    });
            ChannelFuture f = b.connect(ip,port).channel().closeFuture().await();
            return result;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


}
