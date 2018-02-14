package com.littlesky.configbean;

import com.littlesky.rpc.netty.NettyUtil;
import com.littlesky.rpc.rmi.RmiUtil;
import org.springframework.beans.factory.InitializingBean;

import java.io.Serializable;

public class Protocol implements Serializable,InitializingBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8919788684595516884L;

	private String name;

	private String port;

	private String host;
	
	private String contentpath;
	
	

	public String getContentpath() {
		return contentpath;
	}

	public void setContentpath(String contentpath) {
		this.contentpath = contentpath;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		//如果是rmi就开启rmi服务
		if(this.name.equalsIgnoreCase("rmi"))
			RmiUtil.startRmiService(this.host,this.port,"littleskyRmi");
		if(this.name.equalsIgnoreCase("netty"))
		{
			new Thread(new Runnable() {
				@Override
				public void run() {
					try
					{
						NettyUtil.startService(Integer.parseInt(port));
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}

				}
			}).start();
		}

	}
}
