package com.redis.thd.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author dogcome
 *         <p/>
 *         <p>客户端，需要注册服务器并使用jndi</p>
 */
public class HelloClient {

    public static void main(String[] args) {
        /**下面这句话若要加上，则需要进行权限的认证，即增加.policy文件
         * 并且在命令行中使用如下格式
         * java -Djava.security.policy=java.policy test.rmi.HelloServer
         */
        /*System.setSecurityManager(new RMISecurityManager());*/
        try {
            /*注册服务器*/
            String hostName = Configur.getString("HelloServer.RegistryServerName");
            int port = Integer.parseInt(Configur.getString("HelloServer.RegistryServerPort"));
            Registry registry = LocateRegistry.getRegistry(hostName, port);

            Hello hello = (Hello) registry.lookup(Configur.getString("HelloServer.HelloServerName"));
            String message = hello.sayHello();
            System.out.println(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
