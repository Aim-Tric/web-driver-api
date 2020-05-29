package com.gp.webdriverapi.config.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gp.webdriverapi.common.pojo.WdUser;
import com.gp.webdriverapi.system.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * shiro验证权限
 *
 * @author Devonte
 * @date 2020/4/17
 */
public class WebDriverRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) principalCollection.getPrimaryPrincipal();
        WdUser user = userService.getOne(new QueryWrapper<WdUser>()
                .eq("username", username));
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
//        获取用户的权限并添加到信息中
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (authenticationToken.getPrincipal() == null) {
            return null;
        }
        String username = authenticationToken.getPrincipal().toString();
        WdUser user = userService.getOne(new QueryWrapper<WdUser>()
                .eq("username", username));
        if (user != null) {
            SimpleAuthenticationInfo simpleAuthenticationInfo =
                    new SimpleAuthenticationInfo(username, user.getPassword(), getName());
            return simpleAuthenticationInfo;
        }
        return null;
    }
}
