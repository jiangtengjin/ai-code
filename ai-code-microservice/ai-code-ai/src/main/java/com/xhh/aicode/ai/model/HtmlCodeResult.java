package com.xhh.aicode.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

/**
 * html代码生成结果
 */
@Description("生成HTML代码文件的结果")
@Data
public class HtmlCodeResult {

    // html代码
    @Description("HTML代码")
    private String htmlCode;

    // html代码描述
    @Description("生成代码的描述")
    private String description;
}
