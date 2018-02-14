package com.littlesky.loadbalance;

import java.util.HashMap;
import java.util.Map;

public class LoadBalanceRegistry {
    private static final Map<String,LoadBalance> loadBalances = new HashMap<String,LoadBalance>();

    static{
        loadBalances.put("random",new RandomLoadBalance());
        loadBalances.put("roundrobin",new RoundRobinLoadBalance());
    }


    public static LoadBalance getLoadBalances(String key) throws Exception {
        if(key == null || "".equals(key))
        {
        	key = "random";
        }
    	LoadBalance loadBalance = loadBalances.get(key);
        if(loadBalance == null)
        {
            throw new Exception("not have the loadBalance:"+key);
        }
        return loadBalance.getLoadBalanceInvoke();
    }
}
