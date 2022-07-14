package com.nowcoder.community.controller.advice;

import com.nowcoder.community.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Administrator
 * @date 2022/7/13 21:02
 */
// 该注解的 annotations 表示只扫描带有 Controller 注解的bean
@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionAdvice.class);

    // 参数表示 可以处理的异常的种类
    @ExceptionHandler({Exception.class})
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.error("服务器发生异常：" + e.getMessage());

        for (StackTraceElement element : e.getStackTrace()) {
            LOGGER.error(element.toString());
        }
        // 判断浏览器的请求类型，是同步请求需要服务器返回HTMl页面，还是异步请求需要服务器返回JSON字符串
        String xRequestedWith = request.getHeader("x-requested-with");
        // 异步请求
        if ("XMLHttpRequest".equals(xRequestedWith)) {
            response.setContentType("application/json;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(CommunityUtil.getJSONString(1,"服务器异常！"));
        } else {
            // 普通请求
            response.sendRedirect(request.getContextPath() + "/error");
        }

    }
}
