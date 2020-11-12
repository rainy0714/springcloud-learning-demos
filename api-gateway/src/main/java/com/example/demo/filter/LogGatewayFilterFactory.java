package com.example.demo.filter;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 局部过滤器
 *
 * @Author: tongzhenke
 * @Date: 2020/10/27
 */
@Slf4j
@Component
public class LogGatewayFilterFactory extends AbstractGatewayFilterFactory<LogGatewayFilterFactory.Config> {

    public LogGatewayFilterFactory() {
        super(LogGatewayFilterFactory.Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("consoleLog", "cacheLog");
    }

    /**
     * 自定义过滤逻辑
     *
     * @param config
     * @return
     */
    @Override
    public GatewayFilter apply(LogGatewayFilterFactory.Config config) {
        return ((exchange, chain) -> {
            if (config.isConsoleLog()) {
                log.info("输出控制台日志");
            }
            if (config.isCacheLog()) {
                log.info("输出缓存日志");
            }
            return chain.filter(exchange);
        });
    }

    /**
     * 配置类
     */
    @Data
    @NoArgsConstructor
    public static class Config {
        private boolean consoleLog;
        private boolean cacheLog;
    }

}
