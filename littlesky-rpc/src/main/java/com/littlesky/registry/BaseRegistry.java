package com.littlesky.registry;

import java.util.List;

import com.littlesky.configbean.Protocol;
import com.littlesky.configbean.Reference;
import com.littlesky.configbean.Registry;
import com.littlesky.configbean.Service;

public interface BaseRegistry {
    public boolean registry(Service service, Registry registry, Protocol protocol) throws Exception;
    
    public List<String> getRegistry(Reference reference, Registry registry) throws Exception;
}
