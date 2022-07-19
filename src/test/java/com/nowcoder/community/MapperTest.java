package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 * @date 2022/7/9 14:15
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTest {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    public void testSelectUser() {
        User user = userMapper.selectById(101);
        System.out.println(user);

        user = userMapper.selectByName("lisi");
        System.out.println(user);
        user = userMapper.selectByEmail("233@qq.com");
        System.out.println(user);
    }

    @Test
    public void testInsertUser() {
        User user = new User();
        user.setUsername("zs");
        user.setPassword("123233");
        user.setSalt("333");
        user.setEmail("333@qq.com");
        user.setCreateTime(new Date());

        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getId());
    }

    @Test
    public void testUpdateUser() {
        userMapper.updateStatus(101,1);
        userMapper.updateHeader(150,"http://8080/workspace");
        userMapper.updatePassword(151,"333322223");
    }

    @Test
    public void testSelectPosts() {
        int i = discussPostMapper.selectDiscussPostRows(0);
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(0, 2, 2, 0);
        System.out.println(discussPosts);
    }

}
