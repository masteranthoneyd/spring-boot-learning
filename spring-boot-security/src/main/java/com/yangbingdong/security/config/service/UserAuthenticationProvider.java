package com.yangbingdong.security.config.service;

import com.yangbingdong.security.config.entity.SecurityUser;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义登录验证
 */
@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        if (!"admin".equals(userName)) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        if (!new BCryptPasswordEncoder().matches(password, SecurityUser.DEFAULT.getPassword())) {
            throw new BadCredentialsException("密码不正确");
        }
        if (!SecurityUser.DEFAULT.isAccountNonLocked()){
            throw new LockedException("该用户已被冻结");
        }
        // 角色集合
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + "admin"));
        SecurityUser.DEFAULT.setAuthorities(authorities);
        // 进行登录
        return new UsernamePasswordAuthenticationToken(SecurityUser.DEFAULT, password, authorities);
    }
    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
