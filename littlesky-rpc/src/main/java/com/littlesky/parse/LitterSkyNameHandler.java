package com.littlesky.parse;

import com.littlesky.configbean.Protocol;
import com.littlesky.configbean.Reference;
import com.littlesky.configbean.Registry;
import com.littlesky.configbean.Service;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class LitterSkyNameHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        this.registerBeanDefinitionParser("registry", new RegistryBeandifinitionParser(Registry.class));
        this.registerBeanDefinitionParser("protocol",new ProtocolBeanDefinitionParser(Protocol.class));
        this.registerBeanDefinitionParser("reference",new ReferenceBeanDefinitionParser(Reference.class));
        this.registerBeanDefinitionParser("service",new ServiceBeanDefinitonParser(Service.class));
    }
}
