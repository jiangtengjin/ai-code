package com.xhh.aicode.ai;

import com.xhh.aicode.model.enums.CodeGenTypeEnum;
import dev.langchain4j.service.SystemMessage;

/**
 * AI 代码生成类型智能路由服务
 * 使用结构化输出直接返回枚举类型
 */
public interface AiCodeGenTypeRoutingService {

    @SystemMessage(fromResource = "prompt/codegen-routing-system-prompt.txt")
    CodeGenTypeEnum routeCodeGenType(String userPrompt);

}
