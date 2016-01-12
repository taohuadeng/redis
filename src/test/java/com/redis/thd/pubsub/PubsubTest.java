package redis.thd.pubsub;

import com.redis.thd.MyListener;
import com.redis.thd.RedisUtil;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.Jedis;

/**
 * 发布订阅测试类
 */
public class PubsubTest {
    @Test
    public void testPub() {
        ApplicationContext ac = new ClassPathXmlApplicationContext("spring-context.xml");
        RedisUtil ru = (RedisUtil) ac.getBean("redisUtil");
        final Jedis jedis = ru.getConnection();
        final MyListener listener = new MyListener();
        //可以订阅多个频道
        //订阅得到信息在lister的onMessage(...)方法中进行处理
        //jedis.subscribe(listener, "foo", "watson");

        //也用数组的方式设置多个频道
        //jedis.subscribe(listener, new String[]{"hello_foo","hello_test"});

        //这里启动了订阅监听，线程将在这里被阻塞
        //订阅得到信息在lister的onPMessage(...)方法中进行处理
        jedis.psubscribe(listener, new String[]{"hello_*"});//使用模式匹配的方式设置频道
    }
}
