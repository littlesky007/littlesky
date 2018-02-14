package com.littlesky.configbean;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.littlesky.loadbalance.LoadBalance;
import com.littlesky.redis.LittleSkyChannelLisener;
import com.littlesky.redis.RedisPoolUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.littlesky.proxy.InvocationProxyFactory;
import com.littlesky.registry.BaseRegistyDelegate;

public class Reference implements Serializable,FactoryBean,ApplicationContextAware,InitializingBean
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 285211145275583260L;
	
	private String id;
	
	private String intf;
	
	private String loadbanlance;
	
	private String protocol;

	private ApplicationContext applicationContext;

	//每一个reference都有一个与之对应的负载均衡器
	private LoadBalance loadBalanceInvoke;
	
	//获取该reference的id对应的多个生产者列表
	private volatile List<String> registryServicesInfo;


	private static ExecutorService executorService = Executors.newFixedThreadPool(4);

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIntf() {
		return intf;
	}

	public void setIntf(String intf) {
		this.intf = intf;
	}

	public String getLoadbanlance() {
		return loadbanlance;
	}

	public void setLoadbanlance(String loadbanlance) {
		this.loadbanlance = loadbanlance;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public LoadBalance getLoadBalanceInvoke() {
		return loadBalanceInvoke;
	}

	public void setLoadBalanceInvoke(LoadBalance loadBalanceInvoke) {
		this.loadBalanceInvoke = loadBalanceInvoke;
	}

	public List<String> getRegistryServicesInfo() {
		return registryServicesInfo;
	}

	public void setRegistryServicesInfo(List<String> registryServicesInfo) {
		this.registryServicesInfo = registryServicesInfo;
	}

	@Override
	public Object getObject() throws Exception {

		return new InvocationProxyFactory().newInstance(this,applicationContext);
	}

	@Override
	public Class<?> getObjectType() {
		if(intf != null && !"".equals(intf))
		{
			try {
				return Class.forName(intf);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Registry registry = applicationContext.getBean(Registry.class);
		//Protocol protocol = applicationContext.getBean(Protocol.class);
		registryServicesInfo = new BaseRegistyDelegate(this).getRegistry(registry);
		LittleSkyChannelLisener littleSkyChannelLisener = new LittleSkyChannelLisener(this);
		//启动一个线程，对littleSky频道进行订阅
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				RedisPoolUtil.pubSub(littleSkyChannelLisener,"littleSky");
			}
		});


	}
}
