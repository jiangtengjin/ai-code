package com.xhh.aicode.core.saver;

import com.xhh.aicode.ai.model.HtmlCodeResult;
import com.xhh.aicode.ai.model.MultiFileCodeResult;
import com.xhh.aicode.exception.BusinessException;
import com.xhh.aicode.exception.ErrorCode;
import com.xhh.aicode.model.enums.CodeGenTypeEnum;

import java.io.File;

public class CodeFileSaverExecutor {

    private static final HtmlCodeFileSaverTemplate htmlCodeFileSaverTemplate = new HtmlCodeFileSaverTemplate();

    private static final MultiFileCodeSaverTemplate multiFileCodeSaverTemplate =  new MultiFileCodeSaverTemplate();

    /**
     * 执行代码保存
     * @param codeResult        需要保存的结果对象
     * @param codeGenType       代码生成类型
     * @return                  保存的文件目录对象
     */
    public static File executeCodeSave(Object codeResult, CodeGenTypeEnum codeGenType, Long appId) {
        return switch (codeGenType) {
            case HTML -> htmlCodeFileSaverTemplate.saveCode((HtmlCodeResult) codeResult, appId);
            case MULTI_FILE -> multiFileCodeSaverTemplate.saveCode((MultiFileCodeResult) codeResult, appId);
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的代码生成类型" + codeGenType.getValue());
        };
    }

}
