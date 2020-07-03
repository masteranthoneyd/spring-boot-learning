package com.yangbingdong.security.config.handler;

import com.alibaba.fastjson.JSONObject;
import com.yangbingdong.security.config.entity.SecurityUser;
import com.yangbingdong.security.util.JwtUtil;
import com.yangbingdong.security.util.RestUtil;
import com.yangbingdong.security.web.Response;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:yangbingdong1994@gmail.com">yangbingdong</a>
 * @since
 */
@Component
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        String accessToken = JwtUtil.createAccessToken(securityUser);
        Response<JSONObject> error = Response.ok(new JSONObject().fluentPut("token", accessToken));
        RestUtil.writeJson(response, error, HttpStatus.OK);
    }
}
