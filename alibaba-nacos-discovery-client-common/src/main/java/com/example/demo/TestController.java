package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: tongzhenke
 * @Date: 2020/10/19
 */
@Slf4j
@RestController
public class TestController {

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @GetMapping("testHelloNacos")
    public String testHelloNacos() {
        ServiceInstance serviceInstance = loadBalancerClient.choose("alibaba-nacos-discovery-server");
        String url = serviceInstance.getUri() + "/helloNacos?name=" + "haha";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        return "Invoke : " + url + ", return : " + result;
    }

}
