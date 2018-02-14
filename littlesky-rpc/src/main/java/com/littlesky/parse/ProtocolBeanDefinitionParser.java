package com.littlesky.parse;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ProtocolBeanDefinitionParser implements BeanDefinitionParser
{
    private Class<?> clazz;

    public ProtocolBeanDefinitionParser(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String name = element.getAttribute("name");
        String port = element.getAttribute("port");
        String host = element.getAttribute("host");
        String contentpath = element.getAttribute("contentpath");
        if(name == null || "".equals(name))
        {
            throw new RuntimeException("Protocol element name not allow null or empty");
        }

        if(host == null || "".equals(host))
        {
            throw new RuntimeException("Protocol element host not allow null or empty");
        }

        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition();
        rootBeanDefinition.setBeanClass(this.clazz);

        rootBeanDefinition.getPropertyValues().add("name",name);
        rootBeanDefinition.getPropertyValues().add("port",port);
        rootBeanDefinition.getPropertyValues().add("host",host);
        rootBeanDefinition.getPropertyValues().add("contentpath",contentpath);
        parserContext.getRegistry().registerBeanDefinition("Protocol"+host,rootBeanDefinition);
        return rootBeanDefinition;
    }
}
