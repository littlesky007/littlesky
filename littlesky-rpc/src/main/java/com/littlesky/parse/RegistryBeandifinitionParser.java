package com.littlesky.parse;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class RegistryBeandifinitionParser implements BeanDefinitionParser {
    private Class<?> clazz;
    public RegistryBeandifinitionParser(Class<?> clazz)
    {
        this.clazz = clazz;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String protocol = element.getAttribute("protocol");
        String address = element.getAttribute("address");
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(this.clazz);
        if(address == null || "".equals(address))
        {
            throw new RuntimeException("Registry Elemen addresst not allow null or empty");
        }
        beanDefinition.getPropertyValues().add("protocol",protocol);
        beanDefinition.getPropertyValues().add("address",address);

        parserContext.getRegistry().registerBeanDefinition("Registry"+address,beanDefinition);
        return beanDefinition;
    }
}
