package com.nowcoder.community.actuator;

import com.nowcoder.community.util.CommunityUtil;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Administrator
 * @date 2022/7/20 15:15
 *
 * 自定义的 端点 配置
 */
@Component
@Endpoint(id = "database")
public class DataBaseEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(DataBaseEndpoint.class);

    @Autowired
    private DataSource dataSource;

    @ReadOperation
    public String CheckConnection() {
        try (
                Connection connection = dataSource.getConnection();
        ) {
            return CommunityUtil.getJSONString(0, "获取连接成功");
        } catch (SQLException e) {
            logger.error("获取连接失败：" + e.getMessage());
            return CommunityUtil.getJSONString(1, "获取连接失败！");
        }

    }
}
