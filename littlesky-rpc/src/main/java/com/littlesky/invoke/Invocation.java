package com.littlesky.invoke;

import com.littlesky.configbean.Reference;

import java.lang.reflect.Method;

public class Invocation {
    private Object proxy;
    private Method method;
    private Object[] args;
    private Reference reference;

    public Reference getReference() {
        return reference;
    }

    public void setReference(Reference reference) {
        this.reference = reference;
    }

    public Object getProxy() {
        return proxy;
    }

    public void setProxy(Object proxy) {
        this.proxy = proxy;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
