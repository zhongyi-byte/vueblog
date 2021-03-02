package com.zhongyi.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhongyi.util.JwtUtils;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;


import io.jsonwebtoken.Claims;


@Component
public class JwtFilter extends AuthenticatingFilter{
    @Autowired
    JwtUtils jwtUtils;
    @Override
    protected AuthenticationToken createToken(ServletRequest request,ServletResponse response) throws Exception{
        HttpServletRequest request2 = (HttpServletRequest)request;
        String jwt = request2.getHeader("Authorization");
        if(!StringUtils.hasLength(jwt))
         return null;
        return new JwtToken(jwt);

    }
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest,ServletResponse servletResponse){
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        String jwt = request.getHeader("Authorization");
        if(!StringUtils.hasLength(jwt)){
            return true;
        }
        else{
            Claims claim = jwtUtils.getClaimsByToken(jwt);
            if(claim == null || jwtUtils.isTokenExpired(claim.getExpiration()){
                throw new ExpiredCredentialsException("token 已经失效,请重新登陆!");
            }
        }
        return executeLogin(servletRequest, servletResponse);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token,AuthenticationException e,ServletRequest servletRequest,ServletResponse servletResponse){
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        Throwable throwable = e.getCause() == null ? e : e.getCause();
        Result result = Result.fail(throwable.getMessage());
        String json = JSONUtil.toJsonStr(result);

        try {
            httpServletResponse.getWriter().print(json);
        } catch (IOException ioException) {

        }
        return false;
    }
    
}
