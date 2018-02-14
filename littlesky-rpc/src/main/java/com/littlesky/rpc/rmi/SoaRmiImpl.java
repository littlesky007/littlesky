package com.littlesky.rpc.rmi;

import com.littlesky.common.ParamUtil;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class SoaRmiImpl extends UnicastRemoteObject implements SoaRmiInter {

    public SoaRmiImpl() throws RemoteException{

    }

    @Override
    public String invoke(String param) throws InvocationTargetException, IllegalAccessException {
           // JSONObject paramObj = JSONObject.parseObject(param);
           return ParamUtil.invokeMethod(param);
    }
}
