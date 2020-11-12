package com.example.demo.router;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * 动态更新路由网关service
 * ①实现一个spring提供的事件推送接口ApplicationEventPublisherAware
 * ②提供动态路由的基础方法，可通过获取bean操作该类的方法，该类提供新增路由、更新路由、删除路由，然后实现发布的功能
 * @Author: tongzhenke
 * @Date: 2020/10/26
 */
@Slf4j
@Service
public class DynamicRouteServiceImpl implements ApplicationEventPublisherAware {

    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;

    /**
     * 发布事件
     */
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 删除路由
     *
     * @param id
     * @return
     */
    public String delete(String id) {
        try {
            log.info("网关删除路由 {}", id);
            this.routeDefinitionWriter.delete(Mono.just(id));
            return "删除路由成功";
        } catch (Exception e) {
            return "删除路由失败";
        }
    }

    /**
     * 更新路由
     *
     * @param routeDefinition
     * @return
     */
    public String update(RouteDefinition routeDefinition) {
        try {
            log.info("网关更新路由:\n\r{}", routeDefinition);
            this.routeDefinitionWriter.delete(Mono.just(routeDefinition.getId()));
        } catch (Exception e) {
            return "更新路由失败，找不到该路由：" + routeDefinition.getId();
        }
        try {
            addRoute(routeDefinition);
            return "更新路由成功";
        } catch (Exception e) {
            return "更新路由失败";
        }
    }

    private void addRoute(RouteDefinition routeDefinition) {
        log.info("网关增加路由:\n\r{}", routeDefinition);
        this.routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
        this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }

    /**
     * 增加路由
     * @param routeDefinition
     * @return
     */
    public String add(RouteDefinition routeDefinition){
        addRoute(routeDefinition);
        return "增加路由成功";
    }

}
