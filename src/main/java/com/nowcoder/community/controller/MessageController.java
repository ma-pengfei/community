package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Message;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @author Administrator
 * @date 2022/7/13 15:39
 */
@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/letter/list", method = RequestMethod.GET)
    public String getLetterList(Model model, Page page) {
        // 获取当前用户
        User user = hostHolder.getUser();
        // 分页信息
        page.setLimit(5);
        page.setPath("/letter/list");
        page.setRows(messageService.findConversationCount(user.getId()));

        // 会话列表
        List<Message> conversationList = messageService.findConversations(
                user.getId(), page.getOffset(), page.getLimit());

        List<Map<String, Object>> conversations = new ArrayList<>();
        if (conversationList != null) {
            for (Message message : conversationList) {
                Map<String, Object> conversation = new HashMap<>();
                // 会话
                conversation.put("message", message);
                // 未读消息
                int unreadCount = messageService.findUnreadLetterCount(user.getId(), message.getConversationId());
                conversation.put("unreadCount", unreadCount);
                // 会话消息
                int letterCount = messageService.findLetterCount(message.getConversationId());
                conversation.put("letterCount", letterCount);
                // 会话好友
                int targetId = user.getId() == message.getToId() ? message.getFromId() : message.getToId();
                User target = userService.findUserById(targetId);
                conversation.put("target", target);
                conversations.add(conversation);
            }
        }
        model.addAttribute("conversations", conversations);
        int unreadLetterCount = messageService.findUnreadLetterCount(user.getId(), null);
        model.addAttribute("unreadLetters", unreadLetterCount);

        return "/site/letter";
    }

    @RequestMapping(path = "/letter/detail/{conversationId}", method = RequestMethod.GET)
    public String getLetterDetail(@PathVariable("conversationId") String conversationId, Page page, Model model) {

        Integer.valueOf("abc");

        // 分页信息
        page.setPath("/letter/detail/" + conversationId);
        page.setLimit(5);
        page.setRows(messageService.findLetterCount(conversationId));

        List<Message> letters = messageService.findLetters(conversationId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> letterList = new ArrayList<>();
        if (letters != null) {
            for (Message message : letters) {
                Map<String, Object> letter = new HashMap<>();
                letter.put("message", message);
                User from = userService.findUserById(message.getFromId());
                letter.put("from", from);
                letterList.add(letter);
            }
        }
        model.addAttribute("letters", letterList);
        // 私信目标
        model.addAttribute("target", getLetterTarget(conversationId));
        // 设置已读
        List<Integer> ids = getUnreadLetterIds(letters);
        if (!ids.isEmpty()) {
            messageService.readMessage(ids);
        }
        return "/site/letter-detail";
    }

    private User getLetterTarget(String conversationId) {
        String[] ids = conversationId.split("_");
        for (String id : ids) {
            if (Integer.parseInt(id) != hostHolder.getUser().getId()) {
                return userService.findUserById(Integer.parseInt(id));
            }
        }
        return null;
    }

    private List<Integer> getUnreadLetterIds(List<Message> letters) {
        List<Integer> ids = new ArrayList<>();
        if (letters != null) {
            for (Message letter : letters) {
                if (letter.getToId() == hostHolder.getUser().getId() && letter.getStatus() == 0) {
                    ids.add(letter.getId());
                }
            }
        }
        return ids;
    }

    @RequestMapping(path = "/letter/send", method = RequestMethod.POST)
    @ResponseBody
    public String sendLetter(String toName, String content) {

        Integer.valueOf("abc");
        User target = userService.findUserByName(toName);
        if (target == null) {
            return CommunityUtil.getJSONString(1, "目标用户不存在！");
        }
        // 初始化message
        Message message = new Message();
        message.setContent(content);
        message.setCreateTime(new Date());
        message.setStatus(0);
        message.setFromId(hostHolder.getUser().getId());
        message.setToId(target.getId());
        if (message.getToId() > message.getFromId()) {
            message.setConversationId(message.getFromId() + "_" + message.getToId());
        } else {
            message.setConversationId(message.getToId() + "_" + message.getFromId());
        }
        messageService.addMessage(message);

        return CommunityUtil.getJSONString(0);
    }
}
