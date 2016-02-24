package com.redis.thd.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author fish
 *         <p/>
 *         <p>远程方法调用接口，定义远程调用方法</p>
 */
public interface Hello extends Remote {

    String sayHello() throws RemoteException;

}

