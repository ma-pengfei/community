package com.nowcoder.community.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Administrator
 * @date 2022/7/10 14:36
 */
public class CommunityUtil {

    // 生成随机字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // MD5加密
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    public static String getJSONString(int code, String... msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        if (msg != null) {
            map.put("msg", msg);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = null;
        try {
             jsonStr = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }
}
