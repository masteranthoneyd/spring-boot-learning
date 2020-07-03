package com.yangbingdong.security.config.filter;

import com.yangbingdong.security.config.entity.SecurityUser;
import com.yangbingdong.security.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:yangbingdong1994@gmail.com">yangbingdong</a>
 * @since
 */

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String tokenHeader = request.getHeader("token");
        if (tokenHeader != null) {
            try {
                Claims claims = Jwts.parser()
                                    .setSigningKey(JwtUtil.secret)
                                    .parseClaimsJws(tokenHeader)
                                    .getBody();

                SecurityUser securityUser = SecurityUser.builder()
                                                        .userId(Long.parseLong(claims.getId()))
                                                        .username(claims.getSubject())
                                                        .build();
                List<GrantedAuthority> authorities = new ArrayList<>();
                /*String authority = claims.get("authorities").toString();
                if(!StringUtils.isEmpty(authority)){
                    List<Map<String,String>> authorityMap = JSONObject.parseObject(authority, List.class);
                    for(Map<String,String> role : authorityMap){
                        if(!StringUtils.isEmpty(role)) {
                            authorities.add(new SimpleGrantedAuthority(role.get("authority")));
                        }
                    }
                }*/
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(securityUser, securityUser.getUserId(), authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (ExpiredJwtException e) {
                e.printStackTrace();
            } catch (UnsupportedJwtException e) {
                e.printStackTrace();
            } catch (MalformedJwtException e) {
                e.printStackTrace();
            } catch (SignatureException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        chain.doFilter(request, response);
    }
}
