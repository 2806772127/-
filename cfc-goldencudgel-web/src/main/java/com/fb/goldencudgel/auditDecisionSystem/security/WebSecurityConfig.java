package com.fb.goldencudgel.auditDecisionSystem.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * @author hu
 * @since 2019-1-5
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/css/**", "/images/**", "/druid/**", "/thirdPartLogin", 
            "/loginTest", "/saveOperReport/saveOperReport", "/saveCreditReport/saveCreditReport", "/it");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        RequestMatcher requestMatcher = new CsrfSecurityRequestMatcher();
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/css/**", "/images/**", "/js/**","/thirdPartLogin").permitAll() // 都可以访问
                .anyRequest().authenticated() //任何请求,登录后可以访问
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/index")
                //.failureUrl("/hasError")
                .permitAll() //登录页面用户任意访问
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .permitAll(); //注销行为任意访问
    }
}
