package com.redis.thd.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author fish
 *         <p/>
 *         <p>服务器端，实现了Hello接口中的方法，用于实现远程调用方法的具体业务逻辑</p>
 */
public class HelloServer extends UnicastRemoteObject implements Hello {
    String name;

    public HelloServer(String s) throws RemoteException {
        super();
        name = s;
    }

    public String sayHello() throws RemoteException {
        // TODO Auto-generated method stub
        return "Hello world!";
    }

    public static void main(String[] args) {
        /**下面这句话若要加上，则需要进行权限的认证，即增加.policy文件
         * 并且在命令行中使用如下格式
         * java -Djava.security.policy=java.policy test.rmi.HelloServer
         */
        /*System.setSecurityManager(new RMISecurityManager());*/
        Registry registry = null;
        try {
            /**启动注册服务器，使用了这个语句就不再需要在命令行环境中
             *启动registry服务了
             */
            registry = LocateRegistry.getRegistry();
            /* 若没有获得连接，则此句会抛出异常，后面在捕获后进行相关处理 */
            registry.list();
            System.out.println("Register the exist server!"); //$NON-NLS-1$
        } catch (RemoteException re) {
            try {
                int port = Integer.parseInt(Configur
                        .getString("HelloServer.RegistryServerPort")); //$NON-NLS-1$
                registry = LocateRegistry.createRegistry(port);
                System.out.println("Create Registry Server!"); //$NON-NLS-1$
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            HelloServer helloServer = new HelloServer("Hello");
            registry.rebind(Configur.getString("HelloServer.HelloServerName"), helloServer); //$NON-NLS-1$
            System.out.println("HelloServer server start!"); //$NON-NLS-1$
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}