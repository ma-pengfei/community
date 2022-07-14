package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @date 2022/7/12 9:17
 */
@Component
public class SensitiveFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensitiveFilter.class);

    // 替换符
    private static final String REPLACEMENT = "***";

    // 根节点
    private TrieNode rootNode = new TrieNode();

    @PostConstruct//在构造器之前执行。而该类会在项目启动阶段被实例化装入容器中
    public void init() {
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is))
        ) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                this.addKeyword(keyword);
            }
        } catch (IOException e) {
            LOGGER.error("读取敏感词文件失败：" + e.getMessage());
        }
    }

    // 将一个敏感词添加到前缀树中
    private void addKeyword(String keyword) {
        TrieNode tmpNode = rootNode;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = tmpNode.getSubNode(c);

            if (subNode == null) {
                // 初始化子节点
                subNode = new TrieNode();
                tmpNode.addSubNode(c, subNode);
            }
            // 指向子节点，进入下一次循环
            tmpNode = subNode;
            // 设置结束标志
            if (i == keyword.length() - 1) {
                tmpNode.setKeywordEnd(true);
            }
        }
    }

    // 过滤敏感词
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        // 指针 1
        TrieNode curNode = rootNode;
        // 指针 2
        int begin = 0;
        // 指针 3
        int end = 0;
        // 结果
        StringBuilder sb = new StringBuilder();

        // 因为 end 指针更快到达末尾，为了加快效率，将其作为终止条件
        while (end < text.length()) {
            char c = text.charAt(end);
            // 跳过符号
            if (isSymbol(c)) {
                // 若指针 1 位于根节点，将此符号计入结果
                if (curNode == rootNode) {
                    sb.append(c);
                    begin++;
                }
                end++;
                continue;
            }

            // 检查下一个节点
            curNode = curNode.getSubNode(c);
            if (curNode == null) {
                // 下面没有节点，说明begin~end不是敏感字符串
                sb.append(text.charAt(begin));
                // 进入下一位置
                end = ++begin;
                // 重新指向根节点
                curNode = rootNode;
            } else if (curNode.isKeywordEnd()) {
                // 当前节点是敏感词的最后一个节点，说明 begin~end是敏感词字符串
                sb.append(REPLACEMENT);
                // 进入到end下一位置
                begin = end++;
                // 重新指向根节点
                curNode = rootNode;
            } else {
                // begin~end的字符串符合条件，但还未检查完，进入下一位置
                end++;
            }
        }
        // 退出循环时，begin~到结尾不是敏感词汇，加入结果
        sb.append(text.substring(begin));
        return sb.toString();
    }

    // 判断是否为符号
    private boolean isSymbol(Character c) {
        // 0x2E80 ~ 0x9FFF 是东亚地区的文字范围
        return ! CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    // 前缀树
    private class TrieNode {

        // 关键词结束标志
        private boolean isKeywordEnd = false;
        // 子节点
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        // 添加子节点
        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        // 获取子节点
        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }
    }
}
