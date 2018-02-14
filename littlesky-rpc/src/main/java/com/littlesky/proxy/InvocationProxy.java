package com.littlesky.proxy;

import com.littlesky.configbean.Reference;
import com.littlesky.invoke.Invocation;
import com.littlesky.invoke.Invoke;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class InvocationProxy implements InvocationHandler {
    private Invoke invoke;

    private Reference reference;
    public InvocationProxy(Invoke invoke,Reference reference) {
        this.invoke = invoke;
        this.reference=reference;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Invocation invocation = new Invocation();
        invocation.setArgs(args);
        invocation.setMethod(method);
        invocation.setProxy(proxy);
        invocation.setReference(reference);
        return invoke.invoke(invocation);
    }
}
