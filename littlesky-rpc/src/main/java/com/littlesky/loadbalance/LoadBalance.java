package com.littlesky.loadbalance;

import com.littlesky.configbean.Reference;

import java.util.List;

public interface LoadBalance {
    public  SelectedNode selectBalanceNode(Reference reference) throws Exception;
    //为了让reference的不同id的index都是从0开始，所以每个不同的id对应的LoadBalance的对象不同，相同的id对应相同低调LoadBalance对象（仅对轮训方式做的特殊处理）
    public LoadBalance getLoadBalanceInvoke();
}
