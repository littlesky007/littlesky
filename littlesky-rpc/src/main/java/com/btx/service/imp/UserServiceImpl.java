package com.btx.service.imp;

import com.btx.service.IUserService;

public class UserServiceImpl implements IUserService{
    @Override
    public void eat(String name,Integer num) {
        System.out.println("begin eating");
    }
}
