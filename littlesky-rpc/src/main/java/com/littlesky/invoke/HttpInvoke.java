package com.littlesky.invoke;

import com.alibaba.fastjson.JSONObject;
import com.littlesky.httputil.HttpRequest;
import com.littlesky.loadbalance.LoadBalance;
import com.littlesky.loadbalance.SelectedNode;

import java.util.List;


public class HttpInvoke implements Invoke{
    @Override
    public String invoke(Invocation invocation) throws Exception {
        LoadBalance loadBalance = invocation.getReference().getLoadBalanceInvoke();
       // List<String> serviceInfo = invocation.getReference().getRegistryServicesInfo();
        SelectedNode selectedNode = loadBalance.selectBalanceNode(invocation.getReference());
        
        JSONObject sendParam = new JSONObject();
        sendParam.put("MethodName", invocation.getMethod().getName());
        sendParam.put("ServiceId", invocation.getReference().getId());
        sendParam.put("ParamTypes", invocation.getMethod().getParameterTypes());
        sendParam.put("ParamValues", invocation.getArgs());
        String context = selectedNode.getContext();
        context = context.startsWith("/") ? context : "/"+context;
        String url = "http://"+ selectedNode.getHost() + ":" + selectedNode.getPort() + context;
        return HttpRequest.sendPost(url, sendParam.toJSONString());
    }
}
