package com.yangbingdong.security.config.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;

/**
 * @author <a href="mailto:yangbingdong1994@gmail.com">yangbingdong</a>
 * @since
 */
@Data
@Builder
public class SecurityUser implements UserDetails {
    private static final long serialVersionUID = 5515277688927061495L;

    private Long userId;
    private String username;
    private String password;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private Collection<GrantedAuthority> authorities;

    public static SecurityUser DEFAULT;

    static {
        DEFAULT = SecurityUser.builder()
                              .userId(666L)
                              .username("admin")
                              .password(new BCryptPasswordEncoder().encode("admin123"))
                              .accountNonExpired(true)
                              .accountNonLocked(true)
                              .credentialsNonExpired(true)
                              .enabled(true)
                              .build();
    }
}
