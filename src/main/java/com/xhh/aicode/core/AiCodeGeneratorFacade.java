package com.xhh.aicode.core;

import cn.hutool.core.util.ObjUtil;
import com.xhh.aicode.ai.AiCodeGeneratorService;
import com.xhh.aicode.ai.model.HtmlCodeResult;
import com.xhh.aicode.ai.model.MultiFileCodeResult;
import com.xhh.aicode.exception.BusinessException;
import com.xhh.aicode.exception.ErrorCode;
import com.xhh.aicode.exception.ThrowUtils;
import com.xhh.aicode.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * AI 代码生成外观类，组合生成和保存功能
 */
@Service
public class AiCodeGeneratorFacade {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    /**
     * 统一的入口，根据类型生成并保存代码
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成代码的类型
     * @return
     */
    public File generateAndSaveCode(String userMessage, CodeGenTypeEnum codeGenTypeEnum) {
        ThrowUtils.throwIf(ObjUtil.isEmpty(codeGenTypeEnum), ErrorCode.SYSTEM_ERROR, "生成类型为空");
        return switch (codeGenTypeEnum) {
            case HTML -> generateAndSaveHtmlCode(userMessage);
            case MULTI_FILE -> generateAndSaveMultiFileCode(userMessage);
            default -> {
                String message = "不支持生成的类型: " + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, message);
            }
        };
    }

    /**
     *  生成 HTML 模式的代码并保存
     * @param userMessage   用户提示词
     * @return              保存的目录
     */
    private File generateAndSaveHtmlCode(String userMessage) {
        HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode(userMessage);
        return CodeFileSaver.saveHtmlCodeResult(result);
    }

    /**
     *  生成 多文件 模式的代码并保存
     * @param userMessage   用户提示词
     * @return              保存的目录
     */
    private File generateAndSaveMultiFileCode(String userMessage) {
        MultiFileCodeResult result = aiCodeGeneratorService.generateMultiFileCode(userMessage);
        return CodeFileSaver.saveMultiFileCodeResult(result);
    }

}
