package com.xhh.aicode.core;

import com.xhh.aicode.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.List;

@SpringBootTest
class AiCodeGeneratorFacadeTest {

    @Resource
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;

    @Test
    void generateAndSaveCode() {
        File file = aiCodeGeneratorFacade.generateAndSaveCode("小蒋的博客系统", CodeGenTypeEnum.MULTI_FILE);
        Assertions.assertNotNull(file);
    }

    @Test
    void generateAndSaveCodeStream() {
        Flux<String> result = aiCodeGeneratorFacade.generateAndSaveCodeStream("个人介绍网站", CodeGenTypeEnum.HTML);
        // 阻塞等待所有数据收集完成
        List<String> block = result.collectList().block();
        Assertions.assertNotNull(block);
        // 验证结果
        String completeContent = String.join("", block);
        Assertions.assertNotNull(completeContent);
    }

}