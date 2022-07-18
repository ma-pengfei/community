package com.nowcoder.community.config;

import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Administrator
 * @date 2022/7/18 9:30
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements CommunityConstant {

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 忽略所有静态资源，可以直接访问
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 授权
        http.authorizeRequests()
                .antMatchers(
                        // 需要权限的访问路径
                        "/user/setting",
                        "/user/upload",
                        "/discuss/add",
                        "/comment/add/**",
                        "/letter/**",
                        "/notice/**",
                        "/like",
                        "/follow",
                        "/unfollow"
                ).hasAnyAuthority(
                        // 需要的权限
                        AUTHORITY_USER,
                        AUTHORITY_ADMIN,
                        AUTHORITY_MODERATOR
                ).antMatchers(
                        "/discuss/top",
                        "/discuss/wonderful"
                ).hasAnyAuthority(
                        AUTHORITY_MODERATOR
                ).antMatchers(
                        "/discuss/delete"
                ).hasAnyAuthority(
                        AUTHORITY_ADMIN
                ).anyRequest().permitAll();// 除此之外的所有请求都放行
//                .and().csrf().disable() // 禁用防止csrf攻击

        // 没有权限或权限不够时的处理
        http.exceptionHandling()
                // 没有权限时需要登录的处理(authenticationEntryPoint：身份验证入口点)
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        // 获取请求方式是 异步的方式 还是 普通的方式
                        String xRequestedWith = request.getHeader("x-requested-with");
                        if ("XMLHttpRequest".equals(xRequestedWith)) {
                            // XMLHttpRequest：期望的是XML数据(现在通常被JSON代替)，表示请求为异步请求方式
                            response.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer = response.getWriter();
                            // 输出 403 状态码 及 相关提示信息 的JSON字符串
                            writer.write(CommunityUtil.getJSONString(403, "你还没有登录！"));

                        } else {
                            // 否则为普通请求，期望的是HTML页面
                            // 跳转至登录页面
                            response.sendRedirect(request.getContextPath() + "/login");
                        }
                    }
                })
                // 登陆了但权限不足的处理(accessDeniedHandler：访问拒绝处理程序)
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                        // 获取请求方式是 异步的方式 还是 普通的方式
                        String xRequestedWith = request.getHeader("x-requested-with");
                        if ("XMLHttpRequest".equals(xRequestedWith)) {
                            // XMLHttpRequest：期望的是XML数据(现在通常被JSON代替)，表示请求为异步请求方式
                            response.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer = response.getWriter();
                            // 输出 403 状态码 及 相关提示信息 的JSON字符串
                            writer.write(CommunityUtil.getJSONString(403, "你没有权限访问该功能"));

                        } else {
                            // 否则为普通请求，期望的是HTML页面
                            // 跳转到错误页面
                            response.sendRedirect(request.getContextPath() + "/denied");
                        }
                    }
                });

        // security底层默认拦截/logout的请求，进行退出处理
        // 但是拦截并退出之后，不会再继续执行项目本身实现的退出操作，需要覆盖它默认的逻辑，使得能够执行项目自己的退出逻辑
        // 修改security默认的拦截退出操作的路径：/logout,覆盖为其他未处理的路径，绕过security自身的逻辑
        http.logout().logoutUrl("/security/logout");
    }
}