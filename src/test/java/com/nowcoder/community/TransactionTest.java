package com.nowcoder.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Administrator
 * @date 2022/7/12 19:14
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class TransactionTest {

    @Autowired
    private TransactionDemo transactionDemo;

    @Test
    public void testTransaction1() {
        Object add = transactionDemo.add();
        System.out.println(add);
    }

    @Test
    public void testTransaction2() {
        Object add = transactionDemo.save();
        System.out.println(add);
    }


}
