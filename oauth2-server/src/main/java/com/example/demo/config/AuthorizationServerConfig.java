package com.example.demo.config;

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

/**
 * 认证服务器配置
 * Created by macro on 2019/9/30.
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    /**
     * 使用密码模式需要配置
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userService);
    }



    /**
     * withClient: //配置client_id
     * secret： //配置client-secret
     * accessTokenValiditySeconds： //配置访问token的有效期
     * accessTokenValiditySeconds： //配置刷新token的有效期
     * redirectUris： //配置redirect_uri，用于授权成功后跳转
     * scopes： //配置申请的权限范围
     * authorizedGrantTypes： //配置grant_type，表示授权类型
     * 下面配置解释：
     * 授权服务器指定客户端（第三方应用）能访问授权服务器
     * 为第三方应用颁发id为admin，密码为admin123456
     * 支持的授权类型为authorization_code（授权码模式）和password（密码模式）
     * 颁发的令牌有效期为1个小时
     * 通过令牌可以访问的 哪些资源服务器（user-service）可以配置多个
     * 访问资源服务器的all权限
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("admin")
                .secret(passwordEncoder.encode("admin123456"))
                .accessTokenValiditySeconds(3600)
                .refreshTokenValiditySeconds(864000)
                .redirectUris("http://www.baidu.com")
                .scopes("all")
                .authorizedGrantTypes("authorization_code", "password")
                .resourceIds("user-service");
    }
}
