package com.example.demo.router;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 路由配置类
 * @Author: tongzhenke
 * @Date: 2020/10/26
 */
@Configuration
public class GatewayConfig {

    public static final long DEFAULT_TIMEOUT = 30000;

    public static String NACOS_SERVER_ADDR;

    public static String NACOS_NAMESPACE;

    public static String NACOS_ROUTE_DATA_ID;

    public static String NACOS_ROUTE_GROUP;

    @Value("${spring.cloud.nacos.config.server-addr}")
    public void setNacosServerAddr(String nacosServerAddr){
        NACOS_SERVER_ADDR = nacosServerAddr;
    }

    @Value("${spring.cloud.nacos.config.namespace}")
    public void setNacosNamespace(String nacosNamespace){
        NACOS_NAMESPACE = nacosNamespace;
    }

    @Value("${spring.cloud.nacos.config.ext-config.dataId}")
    public void setNacosRouteDataId(String routeDataId){
        NACOS_ROUTE_DATA_ID = routeDataId;
    }

    @Value("${spring.cloud.nacos.config.ext-config.group}")
    public void setNacosRouteGroup(String nacosRouteGroup){
        NACOS_ROUTE_GROUP = nacosRouteGroup;
    }

}
