package com.littlesky.proxy;

import com.littlesky.configbean.Protocol;
import com.littlesky.configbean.Reference;
import com.littlesky.invoke.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class InvocationProxyFactory{


    private static final Map<String,Invoke> invokes = new HashMap<String,Invoke>();



    static{
        invokes.put("http",new HttpInvoke());
        invokes.put("rmi",new RmiInvoke());
        invokes.put("netty",new NeetyInvoke());
    }

    public Object newInstance(Reference reference,ApplicationContext applicationContext)
    {
        InvocationProxy invocationProxy = newProxyInstance(reference,applicationContext);
        return Proxy.newProxyInstance(InvocationProxyFactory.class.getClassLoader(),new Class<?>[]{reference.getObjectType()},invocationProxy);
    }

    public InvocationProxy newProxyInstance(Reference reference,ApplicationContext applicationContext)
    {
        String protocalKey = reference.getProtocol();
        if(protocalKey != null && !"".equals(protocalKey))
        {
            return new InvocationProxy(invokes.get(protocalKey),reference);
        }
        else
        {
            //如果在reference里面没有写协议，就使用protocol标签的协议
            Protocol protocol = applicationContext.getBean(Protocol.class);
            if(protocol != null)
                return new InvocationProxy(invokes.get(protocol.getName()),reference);
            //如果既reference既没有协议，也没有protocol标签，就默认使用http协议类型
            return new InvocationProxy(invokes.get("http"),reference);
        }

    }
}
