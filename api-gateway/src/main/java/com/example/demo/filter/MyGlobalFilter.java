package com.example.demo.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局过滤器：统一鉴权之类的
 * @Author: tongzhenke
 * @Date: 2020/10/26
 */
@Slf4j
@Component
public class MyGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String accessToken = exchange.getRequest().getQueryParams().getFirst("access_token");

        //此处逻辑可以与后台用户中心鉴权逻辑连起来，判断access_token是否合法
        if(accessToken == null || !StringUtils.equals("123", accessToken)){
            log.info("access_token为null或不合法");
            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            return exchange.getResponse().setComplete();
        }

        //放行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
