package com.springbootshirodemo.shiro.config;

import com.springbootshirodemo.entity.SysPermission;
import com.springbootshirodemo.entity.SysRole;
import com.springbootshirodemo.entity.UserInfo;
import com.springbootshirodemo.service.IUserInfoService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

public class MyShiroRealm extends AuthorizingRealm {

    @Resource
    public IUserInfoService userInfoService;

    /**
     * 链接权限的实现
     * Shiro 的权限授权是通过继承AuthorizingRealm抽象类，重载doGetAuthorizationInfo();
     * 当访问到页面的时候，链接配置了相应的权限或者 Shiro 标签才会执行此方法否则不会执行，
     * 所以如果只是简单的身份认证没有权限的控制的话，那么这个方法可以不进行实现，直接返回 null 即可。
     * 在这个方法中主要是使用类：SimpleAuthorizationInfo 进行角色的添加和权限的添加。
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        UserInfo userInfo = (UserInfo) principals.getPrimaryPrincipal();
        for (SysRole sysRole : userInfo.getRoleList()) {
            // 查询当前用户的角色
            authorizationInfo.addRole(sysRole.getRole());
            for (SysPermission sysPermission : sysRole.getPermissions()) {
                // 查询当前用户对应的权限
                authorizationInfo.addStringPermission(sysPermission.getPermission());
            }
        }
        return authorizationInfo;
    }

    /**
     * 重写获取用户信息的方法
     * 1. 检查提交的进行认证的令牌信息
     * 2. 根据令牌信息从数据源(通常为数据库)中获取用户信息
     * 3. 对用户信息进行匹配验证
     * 4. 验证通过将返回一个封装了用户信息的AuthenticationInfo实例
     * 5. 验证失败则抛出AuthenticationException异常信息
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("MyShiroRealm.doGetAuthenticationInfo()");
        /**
         * 获取用户输入的用户名
         */
        String userName = (String) token.getPrincipal();

        /**
         * 通过用户名从数据库中查找 user 对象，找到：，没找到：；
         * 实际项目中，可以根据情况做缓存，如果不做，shiro 也有自己的时间间隔机制，2分钟之内不会重复执行该方法
         */
        UserInfo userInfo = userInfoService.findByUserName(userName);
        if (userInfo == null) {
            return null;
        }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                userInfo,   // 用户信息
                userInfo.getPassword(), // 用户密码
                ByteSource.Util.bytes(userInfo.getCredentialsSalt()),   //salt = username + salt
                getName()   //realm name
        );
        return authenticationInfo;
    }
}
