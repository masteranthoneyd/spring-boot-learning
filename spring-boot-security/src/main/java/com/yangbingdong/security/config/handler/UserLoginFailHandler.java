package com.yangbingdong.security.config.handler;

import com.yangbingdong.security.util.RestUtil;
import com.yangbingdong.security.web.Response;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:yangbingdong1994@gmail.com">yangbingdong</a>
 * @since
 */
@Component
public class UserLoginFailHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        // 这些对于操作的处理类可以根据不同异常进行不同处理
        if (exception instanceof UsernameNotFoundException) {
            RestUtil.writeJson(response, Response.error("用户名不存在"), HttpStatus.BAD_REQUEST);
        }
        if (exception instanceof LockedException) {
            RestUtil.writeJson(response, Response.error("用户被冻结"), HttpStatus.BAD_REQUEST);
        }
        if (exception instanceof BadCredentialsException) {
            RestUtil.writeJson(response, Response.error("用户名密码不正确"), HttpStatus.BAD_REQUEST);
        }
    }
}
