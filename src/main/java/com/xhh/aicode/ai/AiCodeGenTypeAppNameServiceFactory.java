package com.xhh.aicode.ai;

import com.xhh.aicode.utils.SpringContextUtil;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AI 代码生成应用名称服务工厂
 */
@Configuration
@Slf4j
public class AiCodeGenTypeAppNameServiceFactory {

    /**
     * 创建 AI 代码生成应用名称服务实例
     * @return
     */
    public AiCodeGenAppNameService createAiCodeGenAppNameService() {
        ChatModel chatModel = SpringContextUtil.getBean("appNameChatModelPrototype", ChatModel.class);
        return AiServices.builder(AiCodeGenAppNameService.class)
                .chatModel(chatModel)
                .build();
    }

    @Bean
    public AiCodeGenAppNameService aiCodeGenAppNameService() {
        return createAiCodeGenAppNameService();
    }
}
