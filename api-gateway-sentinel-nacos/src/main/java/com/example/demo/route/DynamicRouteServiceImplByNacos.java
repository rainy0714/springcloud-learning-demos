package com.example.demo.route;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * 通过nacos下发动态路由配置,监听Nacos中gateway-route配置
 * @Author: tongzhenke
 * @Date: 2020/10/26
 */
@Slf4j
@Component
@DependsOn({"gatewayConfig"})   //依赖于gatewayConfig bean
public class DynamicRouteServiceImplByNacos {

    @Autowired
    private DynamicRouteServiceImpl dynamicRouteService;

    private ConfigService configService;

    @PostConstruct
    public void init() {
        log.info("网关路由初始化...");
        try {
            configService = initConfigService();
            if (configService == null) {
                log.warn("initConfigService fail");
                return;
            }
            String configInfo = configService.getConfig(GatewayConfig.NACOS_ROUTE_DATA_ID, GatewayConfig.NACOS_ROUTE_GROUP, GatewayConfig.DEFAULT_TIMEOUT);
            log.info("获取网关当前配置:\r\n{}", configInfo);
            List<RouteDefinition> definitionList = JSON.parseArray(configInfo, RouteDefinition.class);
            for (RouteDefinition routeDefinition : definitionList) {
                log.info("update route : {} ", routeDefinition.toString());
                dynamicRouteService.add(routeDefinition);
            }
        } catch (Exception e) {
            log.error("初始化网关路由时发生错误", e);
        }
        dynamicRouteByNacosListener(GatewayConfig.NACOS_ROUTE_DATA_ID, GatewayConfig.NACOS_ROUTE_GROUP);
    }

    public void dynamicRouteByNacosListener(String dataId, String group) {
        try {
            configService.addListener(dataId, group, new Listener() {
                @Override
                public Executor getExecutor() {
                    log.info("getExecutor\n\r");
                    return null;
                }

                @Override
                public void receiveConfigInfo(String configInfo) {
                    log.info("进行网关更新:\n\r{}", configInfo);
                    List<RouteDefinition> definitionList = JSON.parseArray(configInfo, RouteDefinition.class);
                    for (RouteDefinition definition : definitionList) {
                        log.info("update route : {}", definition.toString());
                        dynamicRouteService.update(definition);
                    }
                }
            });
        } catch (Exception e) {
            log.error("从nacos接收动态路由配置出错!!!", e);
        }
    }

    /**
     * 初始化网关路由 nacos config
     *
     * @return
     */
    private ConfigService initConfigService() {
        try {
            Properties properties = new Properties();
            properties.setProperty("serverAddr", GatewayConfig.NACOS_SERVER_ADDR);
            properties.setProperty("namespace", GatewayConfig.NACOS_NAMESPACE);
            configService = NacosFactory.createConfigService(properties);
            return configService;
        } catch (Exception e) {
            log.error("初始化网关路由时发生错误：", e);
            return null;
        }
    }

}
