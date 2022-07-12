package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author Administrator
 * @date 2022/7/11 21:28
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断拦截器拦截到的请求是不是一个方法
        if(handler instanceof HandlerMethod) {
            Method method = ((HandlerMethod) handler).getMethod();
            // 获得拦截到的方法上的注解
            LoginRequired annotation = method.getAnnotation(LoginRequired.class);
            if (annotation != null && hostHolder.getUser() == null) {
                // 如果方法上存在LoginRequired类型注解并且当前ThreadLocal中没有user对象
                // 说明需要登录而没有登录，强制重定向到登录页面
                response.sendRedirect(request.getContextPath() + "/login");
                // 并且取消本次请求
                return false;
            }
        }
        return true;
    }
}
