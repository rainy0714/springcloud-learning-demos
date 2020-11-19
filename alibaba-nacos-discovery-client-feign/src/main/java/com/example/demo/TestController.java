package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @Autowired
    Client client;

    @GetMapping("/test")
    public String test() {
        log.info("到了client feign这里的test方法了");
        return client.helloNacos("Feign invoke Finish");
    }

    @FeignClient("alibaba-nacos-discovery-server")
    interface Client {
        @GetMapping("/helloNacos")
        String helloNacos(@RequestParam String name);
    }

}
