package com.btx.service;

import org.springframework.stereotype.Service;


public class UserService implements IUserService{

	@Override
	public void eat(String name, Integer num) {
		System.out.println(name+":"+num);	
	}

}
