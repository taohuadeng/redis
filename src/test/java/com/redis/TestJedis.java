package redis;

import org.junit.Test;
import redis.clients.jedis.*;

import java.util.Arrays;
import java.util.List;

/**
 * redis测试类
 */
public class TestJedis {

    public static final String KEY_NAME = "name";
    public static final String KEY_AGE = "age";

    @Test
    public void testSetAndGet() {
        Jedis jedis = new Jedis("localhost", 6380);
        String result = jedis.set(KEY_NAME, "TaoHuaDeng");
        System.out.println(result);

        String name = jedis.get(KEY_NAME);
        System.out.println(name);
        jedis.disconnect();
    }

    @Test
    public void testMsetAndMget() {
        Jedis jedis = new Jedis("localhost", 6380);
        String result = jedis.mset(KEY_NAME, "TaoHuaDeng", KEY_AGE, "20");
        System.out.println(result);

        List<String> list = jedis.mget(KEY_NAME, KEY_AGE, "sex");
        for (String value : list) {
            System.out.println(value);
        }

        jedis.disconnect();
    }

    @Test
    public void test1Normal() {
        Jedis jedis = new Jedis("localhost", 6380);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            String result = jedis.set("n" + i, "n" + i);
        }

        long end = System.currentTimeMillis();
        System.out.println("普通：" + ((end - start) / 1000.0) + " 秒");
        jedis.disconnect();
    }


    @Test
    public void test2Trans() {
        Jedis jedis = new Jedis("localhost", 6370);
        long start = System.currentTimeMillis();
        Transaction tx = jedis.multi();
        for (int i = 0; i < 100000; i++) {
            tx.set("t" + i, "t" + i);
        }

        List<Object> results = tx.exec();
        long end = System.currentTimeMillis();
        System.out.println("事务: " + ((end - start) / 1000.0) + " 秒");
        jedis.disconnect();
    }

    @Test
    public void test3Pipelined() {
        Jedis jedis = new Jedis("localhost", 6370);
        Pipeline pipeline = jedis.pipelined();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            pipeline.set("p" + i, "p" + i);
        }

        List<Object> results = pipeline.syncAndReturnAll();
        long end = System.currentTimeMillis();
        System.out.println("管道: " + ((end - start) / 1000.0) + " 秒");
        jedis.disconnect();
    }

    @Test
    public void test3shardSimplePool() {
        List<JedisShardInfo> shards = Arrays.asList(
                new JedisShardInfo("localhost", 6370));

        ShardedJedisPool pool = new ShardedJedisPool(new JedisPoolConfig(), shards);

        ShardedJedis one = pool.getResource();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            String result = one.set("spn" + i, "n" + i);
        }

        long end = System.currentTimeMillis();
        pool.returnResource(one);
        System.out.println("连接池: " + ((end - start) / 1000.0) + " 秒");

        pool.destroy();
    }
}
