package com.nowcoder.community.util;

/**
 * @author Administrator
 * @date 2022/7/10 19:10
 */
public interface CommunityConstant {

    // 激活成功
    int ACTIVATION_SUCCESS = 0;
    // 重复激活
    int ACTIVATION_REPEAT = 1;
    // 激活失败
    int ACTIVATION_FAILURE = 2;

    // 默认失效时间 12小时
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;
    // 记住状态下的失效时间 7天
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 7;

    // 实体类型：回复帖子
    int ENTITY_TYPE_POST = 1;
    // 实体类型：回复评论
    int ENTITY_TYPE_COMMENT = 2;
}
