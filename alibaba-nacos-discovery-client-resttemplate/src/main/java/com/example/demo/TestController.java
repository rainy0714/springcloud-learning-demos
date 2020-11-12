package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: tongzhenke
 * @Date: 2020/10/20
 */
@Slf4j
@RestController
public class TestController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("testByRest")
    public String testByRest() {
        String result = restTemplate.getForObject("http://alibaba-nacos-discovery-server/helloNacos?name=didi", String.class);
        return "Return : " + result;
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
