package com.nowcoder.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Administrator
 * @date 2022/7/9 10:29
 */
@Controller
@RequestMapping("/alpha")
public class AlphaController {

    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello() {
        return "Hello Spring Boot...";
    }

//    @RequestMapping("")
}
