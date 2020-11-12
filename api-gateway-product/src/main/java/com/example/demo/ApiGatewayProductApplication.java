package com.example.demo;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayProductApplication.class, args);
    }

    @Slf4j
    @RestController
    static class ProductController {

        @GetMapping("/info")
        public String info() {
            log.info("invoked product/info");
            return "iPhone 12 pro";
        }

        @SentinelResource(value = "api1", blockHandler = "blockHandlerForApi1")
        @GetMapping("/api1/test1")
        public String api1() {
            log.info("invoked product/api1/test1111");
            return "iPhone 12 pro";
        }

        public String blockHandlerForApi1(BlockException e){
            log.info("invoked product/api1/test1111 被限流了，到这里来处理...");
            return "iPhone 12 pro +++";
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
