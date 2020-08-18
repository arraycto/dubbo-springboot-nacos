package com.liyz.dubbo.common.security.core;

import com.liyz.dubbo.common.security.constant.SecurityConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * 注释:允许授权判断
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2020/8/18 14:29
 */
@Slf4j
@Service
public class AccessDecisionManagerImpl implements AccessDecisionManager {

    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        log.info("@@@@@@@   inter AccessDecisionManagerImpl");
        HttpServletRequest request = ((FilterInvocation) o).getHttpRequest();
        //如果身份是管理员或者是一些不需要验证的url，直接通过
        if (SecurityConstant.BACKSTAGE_ROLE_ADMIN.equals(authentication.getPrincipal())) {
            return;
        }
        for (GrantedAuthority ga : authentication.getAuthorities()) {
            if (ga instanceof AuthGrantedAuthority) {
                AuthGrantedAuthority authGrantedAuthority = (AuthGrantedAuthority) ga;
                String url = authGrantedAuthority.getUrl();
                String method = authGrantedAuthority.getMethod();
                if (matchUrl(url, request)) {
                    if (SecurityConstant.ALL_METHOD.equals(method) || method.equals(request.getMethod())) {
                        return;
                    }
                }
            }
        }
        throw new AccessDeniedException("no right");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    private boolean matchUrl(String url, HttpServletRequest request) {
        if (!StringUtils.isBlank(url)) {
            AntPathRequestMatcher matcher = new AntPathRequestMatcher(url);
            if (matcher.matches(request)) {
                return true;
            } else {
                for (String resource : SecurityConstant.SECURITY_IGNORE_RESOURCES) {
                    matcher = new AntPathRequestMatcher(resource);
                    if (matcher.matches(request)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}