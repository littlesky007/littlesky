package com.littlesky.rpc.rmi;

import com.littlesky.loadbalance.SelectedNode;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RmiUtil {

    public static void startRmiService(String host,String port,String id)
    {
        try {
            SoaRmiInter soaRmiInter = new SoaRmiImpl();
            LocateRegistry.createRegistry(Integer.parseInt(port));
            String name = "rmi://"+host+":"+port+"/"+id;
            Naming.bind(name,soaRmiInter);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }


    public static SoaRmiInter startRmiClient(SelectedNode selectedNode, String id)
    {
        String host = selectedNode.getHost();
        int port = selectedNode.getPort();
        String name = "rmi://"+host+":"+port+"/"+id;
        try {
            return (SoaRmiInter) Naming.lookup(name);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
}
