package com.littlesky.rpc.netty;

public class NettyClientTest {
    public static void main(String[] args) {
        StringBuffer sb = NettyUtil.invoke("127.0.0.1",8888,"你好");
        System.out.println(sb.toString());
    }
}
