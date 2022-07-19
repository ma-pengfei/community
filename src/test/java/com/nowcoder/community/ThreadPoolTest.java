package com.nowcoder.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @date 2022/7/18 22:28
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class ThreadPoolTest {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolTest.class);

    // JDK 普通的线程池
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    // JDK 可执行定时任务的线程池
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

    // Spring 普通的线程池
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    // Spring 可执行定时任务的线程池
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private void sleep(long t) {
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 1.JDK普通的线程池
    @Test
    public void testExecutorService() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                logger.debug("Hello ExecutorService");
            }
        };
        for (int i = 0; i < 20; i++) {
            executorService.submit(runnable);
        }
        sleep(10000);
    }

    // 2.JDK执行定时任务的线程池
    @Test
    public void testScheduledExecutorService() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                logger.debug("Hello ExecutorService");
            }
        };

        // 10s之后开始执行，每次任务间隔一秒执行
        scheduledExecutorService.scheduleWithFixedDelay(runnable, 10000, 1000, TimeUnit.MILLISECONDS);

        sleep(100000);
    }

    // 3.Spring普通的线程池
    @Test
    public void testThreadPoolTaskExecutor() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                logger.debug("Hello ExecutorService");
            }
        };
        for (int i = 0; i < 20; i++) {
            threadPoolTaskExecutor.submit(runnable);
        }
        sleep(10000);
    }

    // 4.Spring执行定时任务的线程池
    @Test
    public void ThreadPoolTaskScheduler() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                logger.debug("Hello ExecutorService");
            }
        };
        Date start = new Date(System.currentTimeMillis() + 10000);
        // 线程任务， 起始时间， 任务间隔
        threadPoolTaskScheduler.scheduleAtFixedRate(runnable, start, 1000);

        sleep(30000);
    }

    // spring普通线程池的简单调用
    @Async
    public void execute1() {
        logger.debug("execute1");
    }
    // spring定时任务线程池的简单调用
    // 初始延迟  固定延迟
    @Scheduled(initialDelay = 10000, fixedDelay = 1000)
    public void execute2() {
        logger.debug("execute2");
    }

    @Test
    public void test1() {
        for (int i = 0; i < 10; i++) {
            execute1();
        }
        sleep(10000);
    }

    @Test
    public void test2() {
        // 无需手动调用定时任务
        sleep(30000);
    }
}
