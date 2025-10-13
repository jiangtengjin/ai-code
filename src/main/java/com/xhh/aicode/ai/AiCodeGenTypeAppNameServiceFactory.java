package com.xhh.aicode.ai;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AI 代码生成应用名称服务工厂
 */
@Configuration
@Slf4j
public class AiCodeGenTypeAppNameServiceFactory {

    @Resource
    private ChatModel chatModel;

    /**
     * 创建 AI 代码生成应用名称服务实例
     * @return
     */
    @Bean
    public AiCodeGenAppNameService aiCodeGenAppNameService() {
        return AiServices.builder(AiCodeGenAppNameService.class)
                .chatModel(chatModel)
                .build();
    }

}
