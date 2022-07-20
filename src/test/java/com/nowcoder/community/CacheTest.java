package com.nowcoder.community;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.service.DiscussPostService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @author Administrator
 * @date 2022/7/20 10:16
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class CacheTest {

    @Autowired
    private DiscussPostService discussPostService;

    @Test
    public void initDataForTest() {
        for (int i = 0; i < 30000; i++) {

            DiscussPost post = new DiscussPost();

            post.setUserId(111);
            post.setTitle("互联网暖春计划");
            post.setContent("今年的就业形势，确实不容乐观。过了个年，仿佛跳水一般，整个讨论区哀鸿遍野！应届生真的没人要了吗！！往届生被优化真的没有办法了吗？！！");
            post.setCreateTime(new Date());
            post.setScore(Math.random() * 2000);
            discussPostService.addDiscussPost(post);
        }
    }

    @Test
    public void testCache() {
        discussPostService.findDiscussPosts(0, 0, 10, 1);
        discussPostService.findDiscussPosts(0, 0, 10, 1);
        discussPostService.findDiscussPosts(0, 0, 10, 1);
        discussPostService.findDiscussPosts(0, 0, 10, 0);
    }
}
