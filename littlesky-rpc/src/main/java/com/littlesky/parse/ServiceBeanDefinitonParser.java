package com.littlesky.parse;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ServiceBeanDefinitonParser implements BeanDefinitionParser {
    private Class<?> clazz;

    public ServiceBeanDefinitonParser(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String intf = element.getAttribute("interface");
        String ref = element.getAttribute("ref");
        String protocol = element.getAttribute("protocol");

        if(intf == null || "".equals(intf))
        {
            throw new RuntimeException("Service element intf not allow null or empty");
        }

        if(ref == null || "".equals(ref))
        {
            throw new RuntimeException("Service element ref not allow null or empty");
        }

        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition();
        rootBeanDefinition.setBeanClass(this.clazz);
        rootBeanDefinition.getPropertyValues().add("intf",intf);
        rootBeanDefinition.getPropertyValues().add("ref",ref);
        rootBeanDefinition.getPropertyValues().add("protocol",protocol);
        parserContext.getRegistry().registerBeanDefinition("Service"+intf+ref,rootBeanDefinition);
        return rootBeanDefinition;
    }
}
