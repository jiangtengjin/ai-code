package com.xhh.aicode.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.xhh.aicode.model.dto.chatHistory.ChatHistoryQueryRequest;
import com.xhh.aicode.model.entity.ChatHistory;
import com.xhh.aicode.model.entity.User;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;

import java.time.LocalDateTime;

/**
 * 对话历史 服务层。
 *
 * @author <a href="https://github.com/jiangtengjin">xhh</a>
 */
public interface ChatHistoryService extends IService<ChatHistory> {

    /**
     * 添加对话历史
     *
     * @param appId         应用ID
     * @param message       对话
     * @param messageType   对话类型
     * @param userId        用户ID
     * @return
     */
    boolean addChatMessage(Long appId, String message, String messageType, Long userId);

    /**
     *  加载对话历史到内存
     *
     * @param appId         应用ID
     * @param chatMemory
     * @param maxCount      最多加载历史的条数
     * @return              成功加载历史的条数
     */
    int loadChatHistoryToMemory(long appId, MessageWindowChatMemory chatMemory, int maxCount);

    /**
     * 根据应用ID查询对话历史（游标）
     *
     * @param appId             应用ID
     * @param pageSize          每页大小
     * @param lastCreateTime    最后创建时间
     * @param loginUser         登录用户
     * @return
     */
    Page<ChatHistory> listAppChatHistoryByPage(Long appId, int pageSize,
                                               LocalDateTime lastCreateTime,
                                               User loginUser);

    /**
     * 根据应用ID删除对话历史
     *
     * @param appId     应用ID
     * @return
     */
    boolean deleteByAppId(Long appId);

    /**
     * 构造查询包装类
     *
     * @param chatHistoryQueryRequest
     * @return
     */
    QueryWrapper getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest);
}
