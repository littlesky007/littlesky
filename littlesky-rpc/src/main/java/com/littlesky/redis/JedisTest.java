package com.littlesky.redis;

import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;

public class JedisTest {
    private  static JedisPool jedisPool;
    public static Long lpush(String key, String... strings) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.lpush(key, strings);
        }
        catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
        finally {
            // returnResource(pool, jedis);
        }
    }

    public void publish(Jedis jedis,String channel,String message)
    {
        jedis.publish(channel,message);
    }

    public void subP(Jedis jedis,String channel)
    {
        PubListener p = new PubListener();
       // jedis.s
    }
    class PubListener extends JedisPubSub
    {

        @Override
        public void proceed(Client client, String... channels) {
            super.proceed(client, channels);
        }

        @Override
        public void onMessage(String channel, String message) {
            System.out.println("订阅的消息：" + message);
            //消息处理函数
            super.onMessage(channel, message);
        }


    }

    public static void main(String[] args) {
        //Jedis jedis = new Jedis("192.168.0.105",6379);

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(10000);
        config.setMaxIdle(10);
        config.setMaxWaitMillis(100000);
        jedisPool = new JedisPool(config, "192.168.0.102", 6379);

        Jedis jedis = jedisPool.getResource();



    }
}
