package com.xhh.aicode.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xhh.aicode.model.entity.ChatHistory;
import com.xhh.aicode.mapper.ChatHistoryMapper;
import com.xhh.aicode.service.ChatHistoryService;
import org.springframework.stereotype.Service;

/**
 * 对话历史 服务层实现。
 *
 * @author <a href="https://github.com/jiangtengjin">xhh</a>
 */
@Service
public class ChatHistoryServiceImpl extends ServiceImpl<ChatHistoryMapper, ChatHistory>  implements ChatHistoryService{

}
