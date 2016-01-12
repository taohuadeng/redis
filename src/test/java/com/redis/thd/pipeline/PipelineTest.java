package redis.thd.pipeline;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.List;

/**
 * redis管道测试
 */
public class PipelineTest {

    @Test
    public void testGet() {
        Jedis jedis = null;
        try {
            jedis = new Jedis("127.0.0.1", 6380);
            Pipeline pipeline = jedis.pipelined();
            pipeline.get("testKey1");
            pipeline.get("testKey2");

            List<Object> list = pipeline.syncAndReturnAll();
            for (Object o : list) {
                System.out.println(o.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.disconnect();
            }
        }
    }

    @Test
    public void testCostTimes() {
        int count = 1000;
        long start = System.currentTimeMillis();
        withoutPipeline(count);
        long end = System.currentTimeMillis();
        System.out.println("WithoutPipeline: " + (end - start));

        start = System.currentTimeMillis();
        usePipeline(count);
        end = System.currentTimeMillis();
        System.out.println("UsePipeline: " + (end - start));
    }

    private void withoutPipeline(int count) {
        Jedis jedis = null;
        try {
            jedis = new Jedis("127.0.0.1", 6380);
            for (int i = 0; i < count; i++) {
                jedis.incr("testKey1");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.disconnect();
            }
        }
    }

    private void usePipeline(int count) {
        Jedis jedis = null;
        try {
            jedis = new Jedis("127.0.0.1", 6380);
            Pipeline pipeline = jedis.pipelined();
            for (int i = 0; i < count; i++) {
                pipeline.incr("testKey2");
            }

            pipeline.sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.disconnect();
            }
        }
    }
}
