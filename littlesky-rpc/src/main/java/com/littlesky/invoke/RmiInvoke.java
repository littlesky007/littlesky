package com.littlesky.invoke;

import com.alibaba.fastjson.JSONObject;
import com.littlesky.loadbalance.LoadBalance;
import com.littlesky.loadbalance.SelectedNode;
import com.littlesky.rpc.rmi.RmiUtil;
import com.littlesky.rpc.rmi.SoaRmiInter;

import java.util.List;

public class RmiInvoke  implements Invoke {
    @Override
    public String invoke(Invocation invocation) throws Exception {
        LoadBalance loadBalance = invocation.getReference().getLoadBalanceInvoke();
      //  List<String> serviceInfo = invocation.getReference().getRegistryServicesInfo();
        //通过负载均衡策略，选择一个服务惊醒调用
        SelectedNode selectedNode = loadBalance.selectBalanceNode(invocation.getReference());

        JSONObject sendParam = new JSONObject();
        sendParam.put("MethodName", invocation.getMethod().getName());
        sendParam.put("ServiceId", invocation.getReference().getId());
        sendParam.put("ParamTypes", invocation.getMethod().getParameterTypes());
        sendParam.put("ParamValues", invocation.getArgs());
//        String context = selectedNode.getContext();
//        context = context.startsWith("/") ? context : "/"+context;
        //String url = "httputil://"+ selectedNode.getHost() + ":" + selectedNode.getPort() + context;
        SoaRmiInter rmiInter = RmiUtil.startRmiClient(selectedNode,"littleskyRmi");
        return rmiInter.invoke(sendParam.toJSONString());
    }
}
