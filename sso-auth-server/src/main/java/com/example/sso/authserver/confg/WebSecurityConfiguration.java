package com.example.sso.authserver.confg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 配置用户角色信息
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // String password = passwordEncoder().encode("123456");
        // auth.inMemoryAuthentication()
        //         .passwordEncoder(passwordEncoder())
        //         .withUser("admin").password(password).roles("ADMIN", "USER")
        //         // .authorities("ROLE_ADMIN", "ROLE_USER")
        //         .and()
        //         .withUser("user1").password(password).roles("USER")
        //         // .authorities("ROLE_USER")
        //         .and()
        //         .withUser("user2").password(password).roles("USER")
        //         // .authorities("ROLE_USER")
        // ;
        auth.userDetailsService(userDetailsServiceBean())
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() {
        Collection<UserDetails> users = buildUsers();
        return new InMemoryUserDetailsManager(users);
    }

    private Collection<UserDetails> buildUsers() {
        String password = passwordEncoder().encode("123456");
        List<UserDetails> users = new ArrayList<>();
        UserDetails admin = User.withUsername("admin").password(password).roles("ADMIN", "USER").build();
        UserDetails user1 = User.withUsername("user1").password(password).roles("USER").build();
        UserDetails user2 = User.withUsername("user2").password(password).roles("USER").build();
        users.add(admin);
        users.add(user1);
        users.add(user2);
        return users;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/assets/**", "/css/**", "/images/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                // 自定义登录页
                .loginPage("/login")
                .and()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .anyRequest()
                .authenticated()
                .and().csrf().disable().cors();
    }

}
