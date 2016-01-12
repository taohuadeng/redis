package com.redis.thd;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


/**
 * 连接和使用redis资源的工具类
 *
 * @author watson
 * @version 0.5
 */
public class RedisUtil {

    /**
     * 数据源
     */
    private JedisPool jedisPool;

    /**
     * 获取数据库连接
     *
     * @return conn
     */
    public Jedis getConnection() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jedis;
    }

    /**
     * 关闭数据库连接
     */
    public void closeConnection(Jedis jedis) {
        if (null != jedis) {
            try {
                jedisPool.returnResource(jedis);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置连接池
     */
    public void setJedisPool(JedisPool JedisPool) {
        this.jedisPool = JedisPool;
    }

    /**
     * 获取连接池
     *
     * @return 数据源
     */
    public JedisPool getJedisPool() {
        return jedisPool;
    }
} 
