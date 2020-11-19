package com.example.demo;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ApiGatewayProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayProductApplication.class, args);
    }

    @Slf4j
    @RestController
    static class ProductController {

        @Autowired
        Client client;

        @FeignClient("alibaba-nacos-discovery-client-feign")
        interface Client {
            @GetMapping("/test")
            String test();
        }

        @GetMapping("/api1/test1")
        public String api1() {
            log.info("invoked product/api1/test1 用feign调用方法玩玩");
            return client.test();
//            return "HUAWEI Mate 40";
        }

        @GetMapping("/api1/test2")
        public String api2() {
            log.info("invoked product/api1/test2222");
            return "iPhone 12 pro";
        }

        @GetMapping("/api2/test3")
        public String api3() {
            log.info("invoked product/api2/test3333");
            return "iPhone 12 pro";
        }

        @GetMapping("/api2/test4")
        public String api4() {
            log.info("invoked product/api2/test4444");
            return "iPhone 12 pro";
        }

    }

}
