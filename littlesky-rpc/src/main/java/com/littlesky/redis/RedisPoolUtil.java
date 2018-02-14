package com.littlesky.redis;

import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.util.ArrayList;
import java.util.List;

public class RedisPoolUtil {
    private static JedisPoolConfig config = new JedisPoolConfig();
    private static JedisPool jedisPool = null;
    static{
        config.setMaxTotal(10000);
        config.setMaxIdle(10);
        config.setMaxWaitMillis(100000);
        config.setTestOnBorrow(true);
    }

    public static void createRedisPool(String host,int port)
    {
        if(RedisPoolUtil.jedisPool == null) {
            jedisPool = new JedisPool(config, host, port);
        }
    }

    public static void publish(String channel, String message)
    {
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            jedis.publish(channel,message);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        finally {
            returnResource(jedisPool, jedis);
        }
    }


    public static void pubSub(JedisPubSub jedisPubSub, String channel)
    {
        Jedis jedis = null;
        jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            jedis.subscribe(jedisPubSub,channel);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        finally {
            returnResource(jedisPool,jedis);
        }
    }

    public static Long lpush(String key,String... value)
    {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.lpush(key, value);
        }
        catch (Exception e) {
            return 0L;
        }
        finally {
            returnResource(jedisPool, jedis);
        }

    }

    public static void returnResource(JedisPool pool, Jedis redis) {
        if (redis != null) {
            redis.close();
        }
    }


    public static List<String> lrange(String key)
    {
        Jedis jedis = null;
        try
        {
        	
            jedis = jedisPool.getResource();
            return jedis.lrange(key,0,-1);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        finally {
            returnResource(jedisPool,jedis);
        }

    }

    public static boolean isExists(String key)
    {
        Jedis jedis = null;
        boolean value = false;
        try {
            jedis = jedisPool.getResource();
            value = jedis.exists(key);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        finally {
            returnResource(jedisPool, jedis);
        }
        return value;
    }

    public static void del(String key)
    {
        Jedis jedis = null;

        try
        {
            jedis = jedisPool.getResource();
           jedis.del(key);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            returnResource(jedisPool, jedis);
        }
    }

    /**
     * 想办法给该方法加上分布式锁
     * @param ref
     * @param values
     * @return
     * @throws Exception
     */
    public static boolean push(String ref,JSONObject values) throws Exception
    {
        if(values == null)
        {
            throw new Exception("redis not allow put none values");
        }
        boolean isExist = RedisPoolUtil.isExists(ref);
        if(isExist)
        {
            String valueKey = null;
            //get values key
            for(String key:values.keySet())
            {
                valueKey = key;
            }
            List<String> oldServerObjs = RedisPoolUtil.lrange(ref);
            List<String> newServerObjs = new ArrayList<String>();

            boolean hasFlag = false;
            for(String node : oldServerObjs)
            {
                JSONObject serverObj = JSONObject.parseObject(node);
                //original jsonObject has the new jsonObject
                if(serverObj.containsKey(valueKey))
                {
                    hasFlag = true;
                    newServerObjs.add(values.toJSONString());
                }
                else
                {
                    newServerObjs.add(node);
                }
            }
            if(hasFlag == false)
            {
                newServerObjs.add(values.toJSONString());
            }
            RedisPoolUtil.del(ref);
            String newArray[] = new String[newServerObjs.size()];
            newServerObjs.toArray(newArray);
            return RedisPoolUtil.lpush(ref,newArray) > 0 ? true : false;
        }
        else
        {
            return RedisPoolUtil.lpush(ref,values.toJSONString()) > 0 ? true : false;
        }
    }
    
    public static void main(String[] args) {
//    	RedisPoolUtil.createRedisPool("47.94.229.142",6379);
//    	
//    	List<String> list = RedisPoolUtil.lrange("userService1");
//    	for(String s:list)
//    	{
//    		System.out.println(s);
//    	}
    	
    	Jedis jedis = new Jedis("47.94.229.142",6379);
    	List<String> list = jedis.lrange("userService1",0,-1);
    	for(String s:list)
    	{
    		System.out.println(s);
    	}
	}
}
