package com.littlesky.invoke;

import com.alibaba.fastjson.JSONObject;
import com.littlesky.loadbalance.LoadBalance;
import com.littlesky.loadbalance.SelectedNode;
import com.littlesky.rpc.netty.NettyUtil;

import java.util.List;

public class NeetyInvoke implements Invoke {
    @Override
    public String invoke(Invocation invocation) throws Exception {

        LoadBalance loadBalance = invocation.getReference().getLoadBalanceInvoke();
      //  List<String> serviceInfo = invocation.getReference().getRegistryServicesInfo();
        //通过负载均衡策略，选择一个服务进行调用
        SelectedNode selectedNode = loadBalance.selectBalanceNode(invocation.getReference());

        if(selectedNode == null)
        {
            throw new Exception("没有对应的生产者");
        }
        JSONObject sendParam = new JSONObject();
        sendParam.put("MethodName", invocation.getMethod().getName());
        sendParam.put("ServiceId", invocation.getReference().getId());
        sendParam.put("ParamTypes", invocation.getMethod().getParameterTypes());
        sendParam.put("ParamValues", invocation.getArgs());

        StringBuffer result = NettyUtil.invoke(selectedNode.getHost(),selectedNode.getPort(),sendParam.toJSONString());

        return result==null ? null : result.toString();
    }
}
