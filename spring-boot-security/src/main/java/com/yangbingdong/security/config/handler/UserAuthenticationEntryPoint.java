package com.yangbingdong.security.config.handler;

import com.yangbingdong.security.util.RestUtil;
import com.yangbingdong.security.web.Response;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:yangbingdong1994@gmail.com">yangbingdong</a>
 * @since
 *
 * 用户未登录处理
 */
@Component
public class UserAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        Response<Void> error = Response.error("请先登录");
        RestUtil.writeJson(response, error, HttpStatus.UNAUTHORIZED);
    }
}
