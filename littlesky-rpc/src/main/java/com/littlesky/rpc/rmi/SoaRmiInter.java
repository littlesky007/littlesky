package com.littlesky.rpc.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SoaRmiInter extends Remote{
    public String invoke(String param) throws Exception;
}
