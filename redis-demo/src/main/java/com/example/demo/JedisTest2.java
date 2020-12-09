package com.example.demo;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.util.List;

/**
 * @Author tongzhenke
 * @Date 2020/12/7 11:06
 */
public class JedisTest2 {

    public static void main(String[] args) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMinIdle(5);
        jedisPoolConfig.setMaxTotal(20);
        jedisPoolConfig.setMaxIdle(10);

        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "8.129.48.169", 6379, 3000, null);

        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();

            //管道示例
            //管道的命令执行方式：cat redis.txt | redis-cli -h 8.129.48.169 -a password -p 6379 --pipe
            Pipeline pipeline = jedis.pipelined();
            for (int i = 0; i < 5; i++){
                pipeline.incr("pipelineKey");
                pipeline.set("hello" + i, "hello redis");

                //模拟一下管道报错
                pipeline.setbit("hello", -1, true);
            }
            List<Object> results = pipeline.syncAndReturnAll();
            System.out.println(results);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //注意这里不是关闭连接，在JedisPool模式下，Jedis会被归还给资源池。
            if (jedis != null) {
                jedis.close();
            }
        }
    }

}
