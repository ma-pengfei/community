package com.nowcoder.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @date 2022/7/14 10:03
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testString() {
        String redisKey = "test:count";

        redisTemplate.opsForValue().set(redisKey, 100);

        System.out.println(redisTemplate.opsForValue().get(redisKey));
        System.out.println(redisTemplate.opsForValue().increment(redisKey));
        System.out.println(redisTemplate.opsForValue().decrement(redisKey));
    }

    @Test
    public void testHash() {
        String redisKey = "test:user";

        redisTemplate.opsForHash().put(redisKey, "id", 100);
        redisTemplate.opsForHash().put(redisKey, "username", "zs");

        System.out.println(redisTemplate.opsForHash().get(redisKey, "id"));
        System.out.println(redisTemplate.opsForHash().get(redisKey, "username"));
    }

    @Test
    public void testList() {
        String redisKey = "test:ids";

        redisTemplate.opsForList().leftPush(redisKey, 101);
        redisTemplate.opsForList().leftPush(redisKey, 102);
        redisTemplate.opsForList().leftPush(redisKey, 103);

        System.out.println(redisTemplate.opsForList().size(redisKey));
        System.out.println(redisTemplate.opsForList().index(redisKey, 2));
        System.out.println(redisTemplate.opsForList().range(redisKey, 0, 2));

        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
    }

    @Test
    public void testSet() {
        String redisKey = "test:set";

        redisTemplate.opsForSet().add(redisKey, "刘备", "张飞", "关羽", "黄忠");

        System.out.println(redisTemplate.opsForSet().size(redisKey));
        System.out.println(redisTemplate.opsForSet().pop(redisKey));
        System.out.println(redisTemplate.opsForSet().members(redisKey));
    }

    @Test
    public void testZSet() {
        String redisKey = "test:zSet";

        redisTemplate.opsForZSet().add(redisKey, "唐僧", 100);
        redisTemplate.opsForZSet().add(redisKey, "沙僧", 60);
        redisTemplate.opsForZSet().add(redisKey, "猪八戒", 70);
        redisTemplate.opsForZSet().add(redisKey, "孙悟空", 90);

        System.out.println(redisTemplate.opsForZSet().zCard(redisKey));
        System.out.println(redisTemplate.opsForZSet().score(redisKey, "猪八戒"));
        System.out.println(redisTemplate.opsForZSet().reverseRank(redisKey, "猪八戒"));
        System.out.println(redisTemplate.opsForZSet().reverseRange(redisKey, 0, 2));
    }

    @Test
    public void testKeys() {

        redisTemplate.delete("test:ids");

        System.out.println(redisTemplate.hasKey("test:ids"));

        redisTemplate.expire("test:zSet", 10, TimeUnit.SECONDS);
    }

    // 多次访问一个key，每次都需要将key传入过于繁琐
    @Test
    public void testBoundOperations() {
        String redisKey = "test:count";
        BoundValueOperations operations = redisTemplate.boundValueOps(redisKey);

        operations.increment();
        System.out.println(operations.get());
    }

    // 编程式事务
    @Test
    public void testTransactional() {

        Object execute = redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String redisKey = "test:tx";
                // 开启事务
                operations.multi();

                operations.opsForSet().add(redisKey, "zs");
                operations.opsForSet().add(redisKey, "lisi");
                operations.opsForSet().add(redisKey, "ww");

                System.out.println(operations.opsForSet().members(redisKey));

                //提交事务
                return operations.exec();
            }
        });

        System.out.println(execute);
    }

}
