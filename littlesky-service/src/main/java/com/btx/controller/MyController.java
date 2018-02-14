package com.btx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.btx.service.IUserService;

@Controller
@RequestMapping("/mytest")
public class MyController {

	@Autowired
	private IUserService userService;
	
	@RequestMapping("/test")
	public String test()
	{
		userService.eat("张三", 12);
		return "ok";
	}
}
