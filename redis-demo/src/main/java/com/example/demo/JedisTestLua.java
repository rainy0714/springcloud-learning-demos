package com.example.demo;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Arrays;

/**
 * @Author tongzhenke
 * @Date 2020/12/7 11:14
 */
public class JedisTestLua {

    public static void main(String[] args) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMinIdle(5);
        jedisPoolConfig.setMaxTotal(20);
        jedisPoolConfig.setMaxIdle(10);

        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "8.129.48.169", 6379, 3000, null);

        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();

            //lua脚本模拟一个商品减库存的原子操作
            //lua脚本执行方式：redis-cli --eval /tmp/test.lua
            jedis.set("product_count_10086", "15");
            String script = " local count = redis.call('get', KEYS[1]) " +
                    " local a = tonumber(count) " +
                    " local b = tonumber(ARGV[1]) " +
                    " if a >= b then " +
                    " redis.call('set', KEYS[1], a-b) " +
                    " return 1 " +
                    " end " +
                    " return 0 ";
            Object obj = jedis.eval(script, Arrays.asList("product_count_10086"), Arrays.asList("10"));
            System.out.println(obj);

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
