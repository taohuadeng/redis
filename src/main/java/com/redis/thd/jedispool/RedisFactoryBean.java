package com.redis.thd.jedispool;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.lang.reflect.InvocationTargetException;

public class RedisFactoryBean implements MethodInterceptor, FactoryBean<JedisCommands> {
    private JedisPoolConfig jedisPoolConfig;
    private JedisPool jedisPool;
    private Jedis jedis;

    /**
     * Jedis operation interface
     */
    private JedisCommands proxy;
    private String host;
    private int port;
    private String password;

    public RedisFactoryBean(String host, int port) {
        this.host = host;
        this.port = port;
        jedis = new Jedis(host, port);
        proxy = (JedisCommands) new ProxyFactory(JedisCommands.class, this).getProxy();
    }

    public RedisFactoryBean(JedisPoolConfig jedisPoolConfig, String host, int port) {
        this.host = host;
        this.port = port;
        jedisPool = new JedisPool(jedisPoolConfig, host, port);
        proxy = (JedisCommands) new ProxyFactory(JedisCommands.class, this).getProxy();
    }

    public RedisFactoryBean(JedisPoolConfig jedisPoolConfig, String host, int port, String password) {
        this.host = host;
        this.port = port;
        this.password = password;
        jedisPool = new JedisPool(jedisPoolConfig, host, port, 0, password);
        proxy = (JedisCommands) new ProxyFactory(JedisCommands.class, this).getProxy();
    }

    JedisCommands getJedis() {
        return proxy;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (jedisPool == null) {
            return invokeInternal(invocation);
        } else {
            return invokeInternalWithPool(invocation);
        }
    }

    synchronized public Object invokeInternal(MethodInvocation invocation)
            throws Throwable {
        try {
            return invocation.getMethod().invoke(jedis, invocation.getArguments());
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            if (targetException != null) {
                if (targetException instanceof JedisConnectionException) {
                    try {
                        jedis.disconnect();
                    } catch (Exception ee) {
                        // ignore error while disconnect.
                    }

                    jedis = new Jedis(host, port);
                }

                throw targetException;
            }

            throw e;
        }
    }

    public Object invokeInternalWithPool(MethodInvocation invocation) throws Throwable {
        Jedis jedisFromPool = jedisPool.getResource();
        Object result = null;
        try {
            result = invocation.getMethod().invoke(jedisFromPool, invocation.getArguments());
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            if (targetException != null) {
                if (targetException instanceof JedisConnectionException) {
                    returnBrokenJedis(jedisFromPool);
                } else {
                    returnJedis(jedisFromPool);
                }

                throw targetException;
            } else {
                returnJedis(jedisFromPool);
                throw e;
            }
        }

        returnJedis(jedisFromPool);
        return result;
    }

    void returnBrokenJedis(Jedis jedisFromPool) {
        try {
            jedisPool.returnBrokenResource(jedisFromPool);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void returnJedis(Jedis jedisFromPool) {
        try {
            jedisPool.returnResource(jedisFromPool);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public JedisCommands getObject() throws Exception {
        return getJedis();
    }

    @Override
    public Class<JedisCommands> getObjectType() {
        return JedisCommands.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
