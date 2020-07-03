package com.yangbingdong.security.config.service;

import com.yangbingdong.security.config.entity.SecurityUser;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 自定义权限注解验证
 *
 */
@Component
public class UserPermissionEvaluator implements PermissionEvaluator {
    /**
     * hasPermission鉴权方法
     * 这里仅仅判断PreAuthorize注解中的权限表达式
     * 实际中可以根据业务需求设计数据库通过targetUrl和permission做更复杂鉴权
     * 当然targetUrl不一定是URL可以是数据Id还可以是管理员标识等,这里根据需求自行设计
     *
     * @Param authentication  用户身份(在使用hasPermission表达式时Authentication参数默认会自动带上)
     * @Param targetUrl  请求路径
     * @Param permission 请求路径权限
     * @Return boolean 是否通过
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetUrl, Object permission) {
        // 获取用户信息
        SecurityUser selfUserEntity = (SecurityUser) authentication.getPrincipal();

        // 查询用户权限(这里可以将权限放入缓存中提升效率)
        Set<String> permissions = obtainPermissions();

        // 校验是否拥有权限
        return permissions.contains(permission.toString());
    }

    private HashSet<String> obtainPermissions() {
        return new HashSet<>();
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
