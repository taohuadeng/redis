package com.redis.thd.jedispool;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ResourceBundle;

/**
 * Jedis使用commons-pool完成池化实现。
 */
public class TestPool {
    private static JedisPool pool;

    static {
        ResourceBundle bundle = ResourceBundle.getBundle("redis");
        if (bundle == null) {
            throw new IllegalArgumentException(
                    "[redis.properties] is not found!");
        }
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(Integer.valueOf(bundle.getString("redis.pool.maxIdle")));
        config.setMaxWaitMillis(Long.valueOf(bundle.getString("redis.pool.maxWait")));
        config.setTestOnBorrow(Boolean.valueOf(bundle.getString("redis.pool.testOnBorrow")));
        config.setTestOnReturn(Boolean.valueOf(bundle.getString("redis.pool.testOnReturn")));
        pool = new JedisPool(config, bundle.getString("redis.ip"), Integer.valueOf(bundle.getString("redis.port")));
    }

    @Test
    public void testPool01() {
        // 从池中获取一个Jedis对象
        Jedis jedis = pool.getResource();
        String keys = "name";

        // 删数据
        jedis.del(keys);
        // 存数据
        jedis.set(keys, "taohuadeng");
        // 取数据
        String value = jedis.get(keys);

        System.out.println(value);

        // 释放对象池
        pool.returnResource(jedis);
    }

    @Test
    public void testPool02() {
        ApplicationContext ac = new ClassPathXmlApplicationContext("spring-context.xml");
        JedisCommands jedis = (JedisCommands) ac.getBean("jedis");
        String name = jedis.get("name");
        System.out.println(name);
    }

    public static void main(String[] args) {

    }
}
