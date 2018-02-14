package com.littlesky.redis;

import com.alibaba.fastjson.JSONObject;
import com.littlesky.configbean.Reference;
import redis.clients.jedis.Client;
import redis.clients.jedis.JedisPubSub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class LittleSkyChannelLisener extends JedisPubSub {

    private Reference reference;

    public LittleSkyChannelLisener(Reference reference)
    {
        this.reference = reference;
    }


    private void dealServices(String message)
    {
        String[] mess = message.split("@");
        //这是key
        String id = mess[0];


        if(reference.getId().equals(id))
        {
            //这个是value
            String context = mess[1];
            JSONObject contextObj = JSONObject.parseObject(context);
            Set<String> keys =  contextObj.keySet();
            String key =  (String)keys.toArray()[0];
            Collection infos = contextObj.values();
            JSONObject infoObj = (JSONObject) infos.toArray()[0];
            Long timecheck = infoObj.getLong("timecheck");
            Long currentTime = System.currentTimeMillis();
            List<String> registryServicesInfo = reference.getRegistryServicesInfo();

            if(registryServicesInfo==null) {
                registryServicesInfo=new ArrayList<>();
            }
            //如果有，就先将其删除
            for(int i=0;i<registryServicesInfo.size();i++)
            {
                String info = registryServicesInfo.get(i);
                //如果含有该ip
                if(info.contains(key))
                {
                    registryServicesInfo.remove(i);
                    if(registryServicesInfo.size()==0)
                    {
                        RedisPoolUtil.del(reference.getId());
                    }

                    break;
                }
            }
            if(currentTime - timecheck <= 20000)
            {
                registryServicesInfo.add(context);
            }

            reference.setRegistryServicesInfo(registryServicesInfo);
        }

    }
    @Override
    public void onMessage(String channel, String message) {
        System.out.println("littleSky频道传来的消息:"+message);
        dealServices(message);
        super.onMessage(channel, message);
    }

    @Override
    public void proceed(Client client, String... channels) {
        super.proceed(client, channels);
    }


    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        for(int i=0;i<list.size();i++)
        {
            String info = list.get(i);
            if("a".equals(info))
            {
                list.remove(i);
                list.add("aa");
            }
        }

        for(int i=0;i<list.size();i++)
        {
            System.out.println(list.get(i));
        }
    }
}
