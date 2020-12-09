package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author tongzhenke
 * @Date 2020/12/7 17:40
 */
@RestController
public class JedisSentinelIndex {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 测试节点挂了哨兵重新选举新的master节点，客户端是否能动态感知到
     * 新的master选举出来后，哨兵会把消息发布出去，客户端实际上是实现了一个消息监听机制，
     * 当哨兵把新master的消息发布出去，客户端会立马感知到新master的信息，从而动态切换访问的masterip
     * @throws InterruptedException
     */
    @RequestMapping("/test_sentinel")
    public void testSentinel() throws InterruptedException {
        int i = 1;
        while (true) {
            try {
                stringRedisTemplate.opsForValue().set("zhuge" + i, i + "");
                System.out.println("设置key：" + "zhuge" + i);
                i++;
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

}
