package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: tongzhenke
 * @Date: 2020/10/19
 */
@Slf4j
@RestController
public class HelloController {

    @GetMapping("/helloNacos")
    public String helloNacos(@RequestParam String name) {
        log.info("invoked name = " + name);
        return "Feign Server say hello " + name;
    }

}
