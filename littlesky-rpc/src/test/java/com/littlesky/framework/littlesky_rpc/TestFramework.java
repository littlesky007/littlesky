package com.littlesky.framework.littlesky_rpc;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestFramework {
    public static void main(String[] args) throws Exception{
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
       // System.out.println("sfd");
//        IUserService userService = (IUserService) applicationContext.getBean("userService");
//        userService.eat("张三",25);
    }
}
