package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class AlibabaNacosDiscoveryClientFeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlibabaNacosDiscoveryClientFeignApplication.class, args);
    }

    @Slf4j
    @RestController
    static class TestController {

        @Autowired
        Client client;

        @GetMapping("test")
        public String test() {
            return client.helloNacos("Feign invoke haha");
        }

    }

    @FeignClient("alibaba-nacos-discovery-server")
    interface Client {

        @GetMapping("/helloNacos")
        String helloNacos(@RequestParam String name);
    }

}
