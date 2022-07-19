package com.nowcoder.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Administrator
 * @date 2022/7/19 8:57
 */
@Configuration
@EnableScheduling
@EnableAsync
public class ThreadPoolConfig {
}
