package com.littlesky.loadbalance;

import com.alibaba.fastjson.JSONObject;
import com.littlesky.configbean.Reference;
import com.littlesky.redis.RedisPoolUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RoundRobinLoadBalance implements LoadBalance {

    private Integer index = -1;

    @Override
    public SelectedNode selectBalanceNode(Reference reference) throws Exception {

        List<String> serviceNodes = reference.getRegistryServicesInfo();
        List<String> serviceNodesCopy = new ArrayList<>(serviceNodes.size());
        serviceNodesCopy.addAll(serviceNodes);
        if(serviceNodesCopy == null ||serviceNodesCopy.size()<=0 )
        {
            throw new Exception("没有对应的生产者");
        }
        for(int i=0;i<serviceNodesCopy.size();i++)
        {
            JSONObject node;
            synchronized (index)
            {
                if(index == serviceNodesCopy.size()-1)
                {
                    index = -1;
                }
                String nodeInfo = serviceNodesCopy.get(++index);
                node = JSONObject.parseObject(nodeInfo);
            }
            Collection valus = node.values();
            Object[] valusArr = valus.toArray();
            JSONObject obj = JSONObject.parseObject(valusArr[0].toString());

            Long timeCheck = obj.getLong("timecheck");
            Long currentTime = System.currentTimeMillis();
            if(currentTime - timeCheck <= 20000)
            {
                JSONObject protocol = obj.getJSONObject("protocol");
                SelectedNode selectedNode = new SelectedNode();
                selectedNode.setHost(protocol.get("host") != null ? protocol.getString("host"):"");
                selectedNode.setPort(protocol.get("port") != null ? protocol.getInteger("port"):0);
                selectedNode.setContext(protocol.get("contentpath") != null ? protocol.getString("contentpath"):"");
                return selectedNode;
            }
            else
            {
                String tempNode = serviceNodesCopy.get(i);
                String message = reference.getId()+"@"+tempNode;
                RedisPoolUtil.publish("littleSky",message);
                serviceNodesCopy.remove(i);
                continue;
            }
        }
        if(serviceNodesCopy == null ||serviceNodesCopy.size()<=0 )
        {
            throw new Exception("没有对应的生产者");
        }
        return null;

    }

    @Override
    public LoadBalance getLoadBalanceInvoke() {
        return new RoundRobinLoadBalance();
    }
}
