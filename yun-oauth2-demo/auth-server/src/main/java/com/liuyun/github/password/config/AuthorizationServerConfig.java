package com.liuyun.github.password.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * 配置授权服务器
 */
@SuppressWarnings("deprecation")
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private static final String RESOURCE_ID = "resource-server";

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 配置ClientDetailsServiceConfigurer
     * 用于配置客户端信息，只有这些客户端才允许访问授权服务器
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient("client_1")
                .secret("{noop}123456")
                .authorizedGrantTypes("password")
                .scopes("all")
                .resourceIds(RESOURCE_ID);
    }

    /**
     * 配置AuthorizationServerEndpointsConfigurer
     * 1. 配置令牌的存取服务
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                //授权码模式所需要的AuthorizationCodeServices
                .authorizationCodeServices(new InMemoryAuthorizationCodeServices())
                //密码模式所需要的authenticationManager
                .authenticationManager(authenticationManager)
                .tokenStore(new InMemoryTokenStore())
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
    }

    /**
     * 配置AuthorizationServerSecurityConfigurer
     * 用来配置令牌端点的安全约束
     * @param oauthServer
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
    }

}
