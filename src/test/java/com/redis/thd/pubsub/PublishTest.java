package com.redis.thd.pubsub;

import com.redis.thd.MyListener;
import com.redis.thd.RedisUtil;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.Jedis;

/**
 * 发布订阅测试类
 */
public class PublishTest {
    @Test
    public void testPublish() {
        ApplicationContext ac = new ClassPathXmlApplicationContext("spring-context.xml");
        RedisUtil ru = (RedisUtil) ac.getBean("redisUtil");
        Jedis jedis = ru.getConnection();
        jedis.publish("hello_foo", "bar123");
        jedis.publish("hello_test", "hello watson");
        jedis.set("name","idea");
    }
}
