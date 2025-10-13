package com.xhh.aicode.ai;

import dev.langchain4j.service.SystemMessage;

/**
 * AI 代码生成应用名称服务
 */
public interface AiCodeGenAppNameService {

    @SystemMessage(fromResource = "prompt/codegen-app-name-system-prompt.txt")
    String appNameCodeGenType(String userPrompt);

}
