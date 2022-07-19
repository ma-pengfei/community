package com.nowcoder.community.config;

import com.nowcoder.community.quartz.AlphaJob;
import com.nowcoder.community.quartz.PostScoreRefreshJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * @author Administrator
 * @date 2022/7/19 9:50
 */
// 配置：第一次调用时读取配置并存入数据库，之后每次都从数据库中读取
@Configuration
public class QuartzConfig {

    // FactoryBean 可简化Bean的实例化过程：
    // 1.通过FactoryBean封装Bean的实例化过程
    // 2.将FactoryBean装配到Spring容器里
    // 3.将FactoryBean注入到其他的Bean里
    // 4.该Bean得到的是FactoryBean所管理的对象实例

    // Job : 任务
    // 配置JobDetail 任务详情
//    @Bean
    public JobDetailFactoryBean alphaJobDetail() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(AlphaJob.class);
        factoryBean.setName("alphaJob");
        factoryBean.setGroup("alphaJobGroup");
        factoryBean.setDurability(true);// 任务是否长久保存
        factoryBean.setRequestsRecovery(true);// 任务出错是否可以恢复
        return factoryBean;
    }

    // 配置Trigger 触发器
//    @Bean
    public SimpleTriggerFactoryBean alphaTrigger(JobDetail alphaJobDetail) {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(alphaJobDetail);
        factoryBean.setName("alphaTrigger");
        factoryBean.setGroup("alphaTriggerGroup");
        factoryBean.setRepeatInterval(3000);// 执行任务的频率：毫秒
        factoryBean.setJobDataMap(new JobDataMap());// Trigger底层存储Trigger的状态需要的对象
        return factoryBean;
    }

    // 定时计算帖子分数
    @Bean
    public JobDetailFactoryBean postScoreRefreshJobDetail() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(PostScoreRefreshJob.class);
        factoryBean.setName("postScoreRefreshJob");
        factoryBean.setGroup("postScoreRefreshJobGroup");
        factoryBean.setDurability(true);// 任务是否长久保存
        factoryBean.setRequestsRecovery(true);// 任务出错是否可以恢复
        return factoryBean;
    }

    // 配置Trigger 触发器
    @Bean
    public SimpleTriggerFactoryBean postScoreRefreshTrigger(JobDetail postScoreRefreshJobDetail) {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(postScoreRefreshJobDetail);
        factoryBean.setName("postScoreRefreshJobDetailTrigger");
        factoryBean.setGroup("postScoreRefreshJobDetailTriggerGroup");
        factoryBean.setRepeatInterval(1000 * 60 * 5);// 执行任务的频率：毫秒
        factoryBean.setJobDataMap(new JobDataMap());// Trigger底层存储Trigger的状态需要的对象
        return factoryBean;
    }
}
