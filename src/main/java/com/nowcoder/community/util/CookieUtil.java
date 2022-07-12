package com.nowcoder.community.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 * @date 2022/7/11 9:36
 */
public class CookieUtil {

    public static String getCookieValue(HttpServletRequest request, String name) {
        if (request == null || name == null) {
            throw new IllegalArgumentException("参数为空！");
        }

        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
