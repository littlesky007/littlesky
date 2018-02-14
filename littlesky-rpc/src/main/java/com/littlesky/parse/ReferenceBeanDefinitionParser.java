package com.littlesky.parse;

import com.littlesky.loadbalance.LoadBalance;
import com.littlesky.loadbalance.LoadBalanceRegistry;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;


public class ReferenceBeanDefinitionParser implements BeanDefinitionParser {
    private Class<?> clazz;

    public ReferenceBeanDefinitionParser(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String id = element.getAttribute("id");
        String intf = element.getAttribute("interface");
        String loadbalance = element.getAttribute("loadbalance");
        String protocol = element.getAttribute("protocol");

        LoadBalance loadBalanceInvoke = null;
        try {
            loadBalanceInvoke = LoadBalanceRegistry.getLoadBalances(loadbalance);
        } catch (Exception e) {
           // e.printStackTrace();
            throw new RuntimeException(e);
        }

        if(id == null || "".equals(id))
        {
            throw new RuntimeException("Reference element id not allow null or empty");
        }

        if(intf == null || "".equals(intf))
        {
            throw new RuntimeException("Reference element interface not allow null or empty");
        }

//        if(loadbalance == null || "".equals(loadbalance))
//        {
//            throw new RuntimeException("Reference element loadbalance not allow null or empty");
//        }

        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition();
        rootBeanDefinition.setBeanClass(this.clazz);
        rootBeanDefinition.getPropertyValues().add("id",id);
        rootBeanDefinition.getPropertyValues().add("intf",intf);
        rootBeanDefinition.getPropertyValues().add("loadbanlance",loadbalance);
        rootBeanDefinition.getPropertyValues().add("protocol",protocol);
        rootBeanDefinition.getPropertyValues().add("loadBalanceInvoke",loadBalanceInvoke);
  //     rootBeanDefinition.setPrimary(false);
 //       rootBeanDefinition.setScope("prototype");
        parserContext.getRegistry().registerBeanDefinition(id,rootBeanDefinition);
        return rootBeanDefinition;
    }
}
