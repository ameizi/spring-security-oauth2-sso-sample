package com.example.sso.authserver.confg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.annotation.Resource;

/**
 * OAuth2 授权服务器配置
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    // @Autowired
    // private DataSource dataSource;

    private static final String SIGNINGKEY = "123456";

    // InMemoryTokenStore
    private TokenStore inMemoryTokenStore = new InMemoryTokenStore();

    /**
     * 密码加密器
     */
    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * 用户认证 Manager
     */
    @Resource
    private AuthenticationManager authenticationManager;

    /**
     * 用户信息 Service
     */
    @Resource
    private UserDetailsService userDetailsService;

    @Bean
    public JwtTokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(SIGNINGKEY);
        return jwtAccessTokenConverter;
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        services.setSupportRefreshToken(true);
        services.setTokenStore(jwtTokenStore());
        services.setTokenEnhancer(jwtAccessTokenConverter());
        return services;
    }

    @Bean
    AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                // .authorizationCodeServices(authorizationCodeServices())
                // .authenticationManager(authenticationManager)
                // .tokenServices(tokenServices())

                // 或者直接使用下面的方法
                .authenticationManager(authenticationManager)
                .accessTokenConverter(jwtAccessTokenConverter())
                .tokenStore(inMemoryTokenStore)
                // 需要指定userDetailsService否则在请求refresh_token接口时报userDetailsService is required
                .userDetailsService(userDetailsService)
        ;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 允许表单认证
        security.allowFormAuthenticationForClients()
                .checkTokenAccess("isAuthenticated()")
                .tokenKeyAccess("isAuthenticated()");
    }

    // @Bean
    // public ClientDetailsService jdbcClientDetailsService() {
    //     return new JdbcClientDetailsService(dataSource);
    // }

    /**
     * 配置客户端信息
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(inMemoryClientDetailsService());
        // clients.withClientDetails(jdbcClientDetailsService());
    }

    @Bean
    public ClientDetailsService inMemoryClientDetailsService() throws Exception {
        return new InMemoryClientDetailsServiceBuilder()
                // client oa application
                .withClient("oa")
                .secret(passwordEncoder.encode("oa_secret"))
                .scopes("read_userinfo", "read_contacts")
                .authorizedGrantTypes("client_credentials", "password", "implicit", "authorization_code", "refresh_token")
                .redirectUris("http://localhost:8080/oa/login")
                .accessTokenValiditySeconds(7200)
                .refreshTokenValiditySeconds(86400)
                .autoApprove(true)
                .and()
                // client crm application
                .withClient("crm")
                .secret(passwordEncoder.encode("crm_secret"))
                .scopes("read_userinfo", "read_contacts")
                .authorizedGrantTypes("client_credentials", "password", "implicit", "authorization_code", "refresh_token")
                .redirectUris("http://localhost:8090/crm/login")
                .accessTokenValiditySeconds(7200)
                .refreshTokenValiditySeconds(86400)
                .autoApprove(true)
                .and()
                // client ops application
                .withClient("ops")
                .secret(passwordEncoder.encode("ops_secret"))
                .scopes("read_userinfo", "read_contacts")
                .authorizedGrantTypes("client_credentials", "password", "implicit", "authorization_code", "refresh_token")
                .redirectUris("http://localhost:9080/ops/callback")
                .accessTokenValiditySeconds(7200)
                .refreshTokenValiditySeconds(86400)
                // .autoApprove(true)
                .and()
                .build();
    }

}
