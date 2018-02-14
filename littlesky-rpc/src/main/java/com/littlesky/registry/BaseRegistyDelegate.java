package com.littlesky.registry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.littlesky.configbean.Protocol;
import com.littlesky.configbean.Reference;
import com.littlesky.configbean.Registry;
import com.littlesky.configbean.Service;

public class BaseRegistyDelegate {

    private Service service;

//    private Registry registry;
//
//    private Protocol protocol;
    private Reference reference;


    private static final Map<String,BaseRegistry> registryRegister = new HashMap<String,BaseRegistry>();

    static {
        registryRegister.put("redis",new RedisRegistry());
        registryRegister.put("zookeeper",null);
    }

    public BaseRegistyDelegate(Service service) {
        this.service = service;
    }
    
    
    public BaseRegistyDelegate(Reference reference)
    {
    	this.reference = reference;
    }

    public boolean registry(Registry registry,Protocol protocol) throws Exception {

//        Registry registry = applicationContext.getBean(Registry.class);
//        Protocolotocol protocol = applicationContext.getBean(Protocol.class);
        String protocolKey = registry.getProtocol();
        BaseRegistry baseRegistry = registryRegister.get(protocolKey);
        return baseRegistry.registry(service,registry,protocol);
    }
    
    public List<String> getRegistry(Registry registry) throws Exception
    {
    	String protocolKey = registry.getProtocol();
    	BaseRegistry baseRegistry = registryRegister.get(protocolKey);
    	return baseRegistry.getRegistry(this.reference, registry);
    	
    }

}
