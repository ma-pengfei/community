package com.nowcoder.community;

import com.nowcoder.community.util.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Administrator
 * @date 2022/7/12 10:47
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTest {

    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Test
    public void testFilter() {
        String text = "这里可以☆赌☆博☆，可以☆嫖☆娼☆，可以喝☆酒，可以强☆奸，可以☆吃饭☆。。。";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
    }

}
