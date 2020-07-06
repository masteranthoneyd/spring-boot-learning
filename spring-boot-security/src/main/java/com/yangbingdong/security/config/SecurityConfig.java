package com.yangbingdong.security.config;

import com.yangbingdong.security.config.filter.JsonUsernamePasswordAuthenticationFilter;
import com.yangbingdong.security.config.filter.JwtAuthenticationFilter;
import com.yangbingdong.security.config.handler.UserAccessDeniedHandler;
import com.yangbingdong.security.config.handler.UserAuthenticationEntryPoint;
import com.yangbingdong.security.config.handler.UserLoginFailHandler;
import com.yangbingdong.security.config.handler.UserLoginSuccessHandler;
import com.yangbingdong.security.config.handler.UserLogoutSuccessHandler;
import com.yangbingdong.security.config.service.UserAuthenticationProvider;
import com.yangbingdong.security.config.service.UserPermissionEvaluator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;

import static java.util.Collections.singletonList;

/**
 * @author <a href="mailto:yangbingdong1994@gmail.com">yangbingdong</a>
 * @since
 */
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 自定义登录逻辑验证器
     */
    private final UserAuthenticationProvider userAuthenticationProvider;
    /**
     * 自定义未登录的处理器
     */
    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;
    /**
     * 自定义登录成功处理器
     */
    private final UserLoginSuccessHandler userLoginSuccessHandler;
    /**
     * 自定义登录失败处理器
     */
    private final UserLoginFailHandler userLoginFailHandler;
    /**
     * 自定义注销成功处理器
     */
    private final UserLogoutSuccessHandler userLogoutSuccessHandler;
    /**
     * 自定义暂无权限处理器
     */
    private final UserAccessDeniedHandler userAccessDeniedHandler;
    /**
     * 自定义权限解析
     */
    private final UserPermissionEvaluator permissionEvaluator;

    /**
     * 静态资源不需要走过滤链
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
           .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 不需要认证的 url
                .antMatchers("/hello/**").permitAll()
                // 其他的请求需要认证
                .anyRequest()
                .authenticated()
            .and()
                // 用户未登录处理
                .httpBasic()
                .authenticationEntryPoint(userAuthenticationEntryPoint)
            .and()
                // 关闭默认的登录配置 (UsernamePasswordAuthenticationFilter), 在下面配置自定义的登录 Filter(支持 json 登录)
                .formLogin()
            .disable()
                .logout()
                // 配置注销地址
                .logoutUrl("/user/logout")
                // 配置注销成功处理器
                .logoutSuccessHandler(userLogoutSuccessHandler)
            .and()
                .exceptionHandling()
                // 配置没有权限自定义处理类
                .accessDeniedHandler(userAccessDeniedHandler)
            .and()
                // 开启跨域
                .cors()
                .configurationSource(corsConfigurationSource())
            .and()
                // 取消跨站请求伪造防护
                .csrf()
            .disable()
                // jwt 无状态不需要 session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .headers()
                .cacheControl()
                .disable()
            .and()
                .rememberMe()
            .disable()
            // 自定义 Jwt 登录认证 Filter
            .addFilterAt(jsonUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            // 自定义 Jwt 过滤器
            .addFilterBefore(new JwtAuthenticationFilter(authenticationManagerBean()), JsonUsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 加密方式
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 自定义登录拦截器, 接收 json 登录信息
     */
    @Bean
    public JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordAuthenticationFilter() throws Exception {
        JsonUsernamePasswordAuthenticationFilter filter = new JsonUsernamePasswordAuthenticationFilter();
        filter.setFilterProcessesUrl("/user/login");
        filter.setAuthenticationSuccessHandler(userLoginSuccessHandler);
        filter.setAuthenticationFailureHandler(userLoginFailHandler);
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    /**
     * 注入自定义 PermissionEvaluator
     */
    @Bean
    public DefaultWebSecurityExpressionHandler userSecurityExpressionHandler(){
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setPermissionEvaluator(permissionEvaluator);
        return handler;
    }

    /**
     * 跨域配置
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(singletonList("*"));
        configuration.setAllowedMethods(singletonList("*"));
        configuration.setAllowedHeaders(singletonList("*"));
        configuration.setMaxAge(Duration.ofHours(1));
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }

    /**
     * 共享 AuthenticationManager
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
