package com.littlesky.configbean;

import com.littlesky.registry.BaseRegistyDelegate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.Serializable;

public class Service implements Serializable,InitializingBean,ApplicationContextAware{
	private static final long serialVersionUID = 790951508411238779L;

	private static ApplicationContext applicationContext ;

	private String intf;
	
	private String ref;
	
	private String protocol;

	public String getIntf() {
		return intf;
	}

	public void setIntf(String intf) {
		this.intf = intf;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		Registry registry = applicationContext.getBean(Registry.class);
		Protocol protocol = applicationContext.getBean(Protocol.class);
		new BaseRegistyDelegate(this).registry(registry,protocol);
		
}

//	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext()
	{
		return Service.applicationContext;
	}
}
