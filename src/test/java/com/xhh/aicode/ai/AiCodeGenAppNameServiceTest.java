package com.xhh.aicode.ai;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class AiCodeGenAppNameServiceTest {

    @Resource
    private AiCodeGenTypeAppNameServiceFactory aiCodeGenTypeAppNameServiceFactory;

    @Test
    void appNameCodeGenType() {

        String userPrompt = "做一个简单的个人介绍页面";
        AiCodeGenAppNameService aiCodeGenAppNameService = aiCodeGenTypeAppNameServiceFactory.createAiCodeGenAppNameService();
        String result = aiCodeGenAppNameService.appNameCodeGenType(userPrompt);
        log.info("用户需求: {} -> {}", userPrompt, result);

    }
}