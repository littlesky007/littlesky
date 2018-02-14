package com.littlesky.registry;

import com.alibaba.fastjson.JSONObject;
import com.littlesky.configbean.Protocol;
import com.littlesky.configbean.Reference;
import com.littlesky.configbean.Registry;
import com.littlesky.configbean.Service;
import com.littlesky.redis.RedisPoolUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class RedisRegistry implements BaseRegistry {

	private final ScheduledExecutorService expireExecutor = Executors.newScheduledThreadPool(1);

	//心跳检测默认时间
	private static Long expirePeriod = 20000L;
	@Override
	public boolean registry(Service service, Registry registry, Protocol protocol) throws Exception{
		String address = registry.getAddress();
		if(address == null || "".equals(address))
		{
			throw new Exception("address is not all none");
		}
		else
		{
			String host = address.split(":")[0];
			int port = Integer.valueOf(address.split(":")[1]);
			RedisPoolUtil.createRedisPool(host,port);
			//服务注册的redis的主机和端口
			String serverHost = protocol.getHost();
			String serverPort = protocol.getPort();
			JSONObject serverObj = new JSONObject();

			//服务暴露的主机地址和端口
			String key = serverHost + ":" +serverPort;

			JSONObject obj = new JSONObject();
			obj.put("protocol",JSONObject.toJSONString(protocol));
			obj.put("server",JSONObject.toJSONString(service));

			//服务注册加上当前时间
			obj.put("timecheck",System.currentTimeMillis());
			serverObj.put(key,obj);
			String ref = service.getRef();
			RedisPoolUtil.push(ref,serverObj);

			//服务注册后，向消费者发送消息

			RedisPoolUtil.publish("littleSky",ref+"@"+JSONObject.toJSONString(serverObj));

			System.out.println(ref+"@"+JSONObject.toJSONString(serverObj));
			expireExecutor.scheduleWithFixedDelay(new Runnable() {
				@Override
				public void run() {

					try {
						RedisPoolUtil.push(ref,serverObj);
						JSONObject obj = (JSONObject)serverObj.get(key);
						obj.put("timecheck",System.currentTimeMillis());
						System.out.println(ref+"@"+JSONObject.toJSONString(serverObj)+"注册一次。。");
						//RedisPoolUtil.publish("littleSky",ref+"@"+JSONObject.toJSONString(serverObj));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			},expirePeriod / 2,expirePeriod / 2,TimeUnit.MILLISECONDS);
		}


		return true;
	}

	@Override
	public List<String> getRegistry(Reference reference, Registry registry) throws Exception {
		String address = registry.getAddress();
		if(address == null || "".equals(address))
		{

			throw new Exception("address is not all none");
		}
		else
		{
			//create redis registry
			String host = address.split(":")[0];
			int port = Integer.valueOf(address.split(":")[1]);
			RedisPoolUtil.createRedisPool(host,port);
			String id = reference.getId();
			return RedisPoolUtil.lrange(id);
		}

	}


}
