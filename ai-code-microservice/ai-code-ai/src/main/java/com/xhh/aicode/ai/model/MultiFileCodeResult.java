package com.xhh.aicode.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

/**
 *  多文件代码生成结果
 */
@Description("生成多个代码文件的结果")
@Data
public class MultiFileCodeResult {

    // html文件内容
    @Description("HTML代码")
    private String htmlCode;

    // css文件内容
    @Description("CSS代码")
    private String cssCode;

    // js文件内容
    @Description("JS代码")
    private String jsCode;

    // 文件描述
    @Description("生成代码的描述")
    private String description;
}
