package com.nowcoder.community.service;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Administrator
 * @date 2022/7/14 19:47
 */
@Service
public class FollowService implements CommunityConstant {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    public void follow(int userId, int entityType, int entityId) {

        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {

                String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);

                operations.multi();

                operations.opsForZSet().add(followeeKey, entityId, System.currentTimeMillis());
                operations.opsForZSet().add(followerKey, userId, System.currentTimeMillis());

                return operations.exec();
            }
        });
    }

    public void unFollow(int userId, int entityType, int entityId) {

        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {

                String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);

                operations.multi();

                operations.opsForZSet().remove(followeeKey, entityId);
                operations.opsForZSet().remove(followerKey, userId);

                return operations.exec();
            }
        });
    }

    // 查询关注的实体的数量
    public long findFolloweeCount(int userId, int entityType) {

        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        Long count = redisTemplate.opsForZSet().zCard(followeeKey);
        return count == null ? 0 : count;
    }

    // 查询实体的粉丝的数量
    public long findFollowerCount(int entityId, int entityType) {

        String followeeKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        Long count = redisTemplate.opsForZSet().zCard(followeeKey);
        return count == null ? 0 : count;
    }

    // 查询当前用户是否已关注某实体
    public boolean hasFollowed(int userId, int entityId, int entityType) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().score(followeeKey, entityId) != null;
    }

    // 查询关注的人
    public List<Map<String, Object>> findFolloweeList(int userId, int offset, int limit) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, ENTITY_TYPE_USER);
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followeeKey, offset, offset + limit - 1);
        if (targetIds == null) {
            return null;
        }
        List<Map<String, Object>> followeeList = new ArrayList<>();
        for (Integer id : targetIds) {
            Map<String, Object> followee = new HashMap<>();
            User user = userService.findUserById(id);
            followee.put("user", user);
            Double score = redisTemplate.opsForZSet().score(followeeKey, id);
            followee.put("followTime", new Date(score.longValue()));
            followeeList.add(followee);
        }
        return followeeList;
    }

    // 查询某个用户的粉丝
    public List<Map<String, Object>> findFollowerList(int userId, int offset, int limit) {
        String followerKey = RedisKeyUtil.getFollowerKey(ENTITY_TYPE_USER, userId);
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followerKey, offset, offset + limit - 1);
        if (targetIds == null) {
            return null;
        }
        List<Map<String, Object>> followerList = new ArrayList<>();
        for (Integer id : targetIds) {
            Map<String, Object> follower = new HashMap<>();
            User user = userService.findUserById(id);
            follower.put("user", user);
            Double score = redisTemplate.opsForZSet().score(followerKey, id);
            follower.put("followTime", new Date(score.longValue()));
            followerList.add(follower);
        }
        return followerList;
    }
}
