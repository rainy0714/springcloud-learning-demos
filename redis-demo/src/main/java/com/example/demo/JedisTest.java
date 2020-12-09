package com.example.demo;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Author tongzhenke
 * @Date 2020/12/7 10:27
 */
public class JedisTest {

    public static void main(String[] args) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(10);
        jedisPoolConfig.setMaxTotal(20);
        jedisPoolConfig.setMinIdle(5);

        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "8.129.48.169", 6379, 3000, null);

        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();

            System.out.println(jedis.set("hello", "hello redis"));
            System.out.println(jedis.get("hello"));
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
