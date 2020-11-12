package com.example.demo;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @Author: tongzhenke
 * @Date: 2020/10/27
 */
@Slf4j
@Configuration
public class GatewaySentinelConfiguration {

    private final List<ViewResolver> viewResolvers;
    private final ServerCodecConfigurer serverCodecConfigurer;

    public GatewaySentinelConfiguration(ObjectProvider<List<ViewResolver>> vewResolverProvider, ServerCodecConfigurer serverCodecConfigurer) {
        this.viewResolvers = vewResolverProvider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    /**
     * 初始化一个限流过滤器
     *
     * @return
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public GlobalFilter sentinelGatewayFilter() {
        return new SentinelGatewayFilter();
    }

    /**
     * 配置初始化的限流参数
     *
     * @PostConstruct Java注解：用来修饰非静态void方法
     * 被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器执行一次
     * 在spring中使用，该注解的方法在整个Bean初始化中的执行顺序：
     * Constructor(构造方法) -> @Autowired(依赖注入) -> @PostConstruct(注释的方法)
     */
    @PostConstruct
    public void initGatewayRules() {
        Set<GatewayFlowRule> rules = new HashSet<>();
        //以路由id为限流维度，resource为路由id，count为阈值，interval为统计时间窗口，默认1秒
//        rules.add(new GatewayFlowRule("api-gate-product").setCount(1).setIntervalSec(1));
        //以api分组为限流维度
        rules.add(new GatewayFlowRule("api-gate-product-route1").setCount(1).setIntervalSec(1));
        rules.add(new GatewayFlowRule("api-gate-product-route2").setCount(1).setIntervalSec(1));
        GatewayRuleManager.loadRules(rules);
    }
    /**
     * GatewayFlowRule名词解释
     * resource：资源名称，可以是网关中的 route 名称或者用户自定义的API 分组名称
     * resourceMode：规则是针对 API Gateway 的route（RESOURCE_MODE_ROUTE_ID）还是用户在 Sentinel 中定义的API 分组（RESOURCE_MODE_CUSTOM_API_NAME），默认是route
     * grade：限流指标维度，同限流规则的grade 字段
     * count：限流阈值
     * intervalSec：统计时间窗口，单位是秒，默认是1 秒（目前仅对参数限流生效）
     * controlBehavior：流量整形的控制效果，同限流规则的 controlBehavior 字段，目前支持快速失败和匀速排队两种模式，默认是快速失败
     * burst：应对突发请求时额外允许的请求数目（目前仅对参数限流生效）
     * maxQueueingTimeoutMs：匀速排队模式下的最长排队时间，单位是毫秒，仅在匀速排队模式下生效
     * paramItem：参数限流配置。若不提供，则代表不针对参数进行限流，该网关规则将会被转换成普通流控规则；否则会转换成热点规则。其中的字段：
     *      parseStrategy：从请求中提取参数的策略，目前支持提取来源 IP（PARAM_PARSE_STRATEGY_CLIENT_IP）、Host（PARAM_PARSE_STRATEGY_HOST）、任意 Header（PARAM_PARSE_STRATEGY_HEADER）和任意 URL 参数（PARAM_PARSE_STRATEGY_URL_PARAM）四种模式
     *      fieldName：若提取策略选择 Header 模式或 URL 参数模式，则需要指定对应的 header 名称或 URL 参数名称
     *      pattern 和 matchStrategy：为后续参数匹配特性预留，目前未实现
     */

    /**
     * 配置异常处理器
     *
     * @return
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
        return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
    }

    /**
     * 自定义限流异常返回页面
     */
    @PostConstruct
    public void initBlockHandlers() {
        BlockRequestHandler blockRequestHandler = (serverWebExchange, throwable) -> {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", 0);
            map.put("message", "接口被限流了");
            return ServerResponse.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(map));
        };
        GatewayCallbackManager.setBlockHandler(blockRequestHandler);
    }

    /**
     * 自定义api分组，api维度限流
     */
    @PostConstruct
    public void initCustomizedApis() {
        Set<ApiDefinition> definitions = new HashSet<>();
        //api分组，apiName唯一即可
        ApiDefinition apiDefinition1 = new ApiDefinition("api-gate-product-route1")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                    add(new ApiPathPredicateItem().setPattern("/product/api1/**")
                            .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
                }});
        ApiDefinition apiDefinition2 = new ApiDefinition("api-gate-product-route2")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                    add(new ApiPathPredicateItem().setPattern("/product/api2/test3"));
                }});

        definitions.add(apiDefinition1);
        definitions.add(apiDefinition2);
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }

}
