package com.co.backend.filter;

import com.co.backend.constant.JWTConstant;
import com.co.backend.constant.RedisConstants;
import com.co.backend.model.entity.LoginUser;
import com.co.backend.utils.JwtUtil;
import com.co.backend.utils.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.util.AntPathMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-03-26 22:12
 */

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private RedisCache redisCache;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader(JWTConstant.Authorization);
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        //可携带过期token访问路径
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String requestURI = request.getRequestURI();
        List<String> permitPaths = Arrays.asList(
                "/login",
                "/register",
                "/refreshToken",
                "/question/getQuestionList",
                "/question/getQuestion/**"
        );

        //解析token
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            //是可携带过期token的路径，则将用户认证信息置空
            if (permitPaths.stream().anyMatch(permitPath -> pathMatcher.match(permitPath, requestURI))) {
                SecurityContextHolder.clearContext();
                filterChain.doFilter(request,response);
                return;
            }
            e.printStackTrace();
            throw new RuntimeException("token is err");
        }
        //从redis中获取用户信息
        String redisKey = RedisConstants.LOGIN_CODE_KEY + userid;
        LoginUser loginUser = redisCache.getCacheObject(redisKey);
        if (Objects.isNull(loginUser)) {
            throw new RuntimeException("user unlogin");
        }
        //存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }
}
