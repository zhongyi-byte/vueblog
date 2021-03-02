package com.zhongyi.shiro;



import com.zhongyi.entity.User;
import com.zhongyi.service.UserService;
import com.zhongyi.util.JwtUtils;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.hutool.core.bean.BeanUtil;




@Component
public class AccountRealm extends AuthorizingRealm{
    @Autowired 
    JwtUtils jwtUtils;
    @Autowired 
    UserService userService;
    @Override
    public boolean supports(AuthenticationToken token){
        return token instanceof JwtToken;
    }

    @Override 
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals){
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)throws AuthenticationException{
        JwtToken jwt = (JwtToken)token;
        
        String userId =jwtUtils.getClaimsByToken((String)jwt.getPrincipal()).getSubject();
        User user = userService.getById(Long.parseLong(userId));
        if(user == null){
            throw new UnknownAccountException("账户不存在!");
        }
        if(user.getStatus() == -1){
            throw new LockedAccountException("账户被锁定!");
        }
        AccountProfile profile = new AccountProfile();
        BeanUtil.copyProperties(user, profile);
      
        return new SimpleAuthenticationInfo(profile,jwt.getCredentials(),getName());


    }

}
