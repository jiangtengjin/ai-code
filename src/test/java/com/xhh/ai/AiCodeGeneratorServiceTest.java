package com.xhh.ai;

import com.xhh.aicode.ai.AiCodeGeneratorService;
import com.xhh.aicode.ai.model.HtmlCodeResult;
import com.xhh.aicode.ai.model.MultiFileCodeResult;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = com.xhh.aicode.AiCodeApplication.class)
class AiCodeGeneratorServiceTest {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    @Test
    void generateHtmlCode() {
        HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode("做个小蒋的工作记录小工具");
        Assertions.assertNotNull(result);
    }

    @Test
    void generateMultiFileCode() {
        MultiFileCodeResult multiFileCode = aiCodeGeneratorService.generateMultiFileCode("做个小蒋的留言板");
        Assertions.assertNotNull(multiFileCode);
    }
}
