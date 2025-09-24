package com.xhh.aicode.core;

import cn.hutool.core.util.ObjUtil;
import com.xhh.aicode.ai.AiCodeGeneratorService;
import com.xhh.aicode.ai.model.HtmlCodeResult;
import com.xhh.aicode.ai.model.MultiFileCodeResult;
import com.xhh.aicode.core.parser.CodeParserExecutor;
import com.xhh.aicode.core.saver.CodeFileSaverExecutor;
import com.xhh.aicode.exception.BusinessException;
import com.xhh.aicode.exception.ErrorCode;
import com.xhh.aicode.exception.ThrowUtils;
import com.xhh.aicode.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;

/**
 * AI 代码生成外观类，组合生成和保存功能
 */
@Service
@Slf4j
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
    public File generateAndSaveCode(String userMessage, CodeGenTypeEnum codeGenTypeEnum, Long appId) {
        ThrowUtils.throwIf(ObjUtil.isEmpty(codeGenTypeEnum), ErrorCode.SYSTEM_ERROR, "生成类型为空");
        return switch (codeGenTypeEnum) {
            case HTML -> {
                HtmlCodeResult codeResult = aiCodeGeneratorService.generateHtmlCode(userMessage);
                yield CodeFileSaverExecutor.executeCodeSave(codeResult, codeGenTypeEnum, appId);
            }
            case MULTI_FILE -> {
                MultiFileCodeResult codeResult = aiCodeGeneratorService.generateMultiFileCode(userMessage);
                yield CodeFileSaverExecutor.executeCodeSave(codeResult, codeGenTypeEnum, appId);
            }
            default -> {
                String message = "不支持生成的类型: " + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, message);
            }
        };
    }

    /**
     * 统一的入口，根据类型生成并保存代码（流式）
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成代码的类型
     * @return
     */
    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenTypeEnum codeGenTypeEnum, Long appId) {
        ThrowUtils.throwIf(ObjUtil.isEmpty(codeGenTypeEnum), ErrorCode.SYSTEM_ERROR, "生成类型为空");
        return switch (codeGenTypeEnum) {
            case HTML -> {
                Flux<String> codeStream = aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
                yield processCodeStream(codeStream, codeGenTypeEnum, appId);
            }
            case MULTI_FILE -> {
                Flux<String> codeStream = aiCodeGeneratorService.generateMultiFileCodeStream(userMessage);
                yield processCodeStream(codeStream, codeGenTypeEnum, appId);
            }
            default -> {
                String message = "不支持生成的类型: " + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, message);
            }
        };
    }

    /**
     *  通用流式代码处理方式
     * @param result        代码流
     * @param codeGenType   代码生成类型
     * @return              流式响应
     */
    private Flux<String> processCodeStream(Flux<String> result, CodeGenTypeEnum codeGenType, Long appId) {
        // 当流式返回生成代码后，再保存代码
        StringBuilder codeBuilder = new StringBuilder();
        // 实时收集代码片段
        return result
                .doOnNext(codeBuilder::append)
                .doOnComplete(() -> {
                    try {
                        // 流式返回后保存代码
                        String completeCode = codeBuilder.toString();
                        // 使用执行器解析代码
                        Object parseResult = CodeParserExecutor.executeCodeParse(completeCode, codeGenType);
                        // 保存到文件
                        File saveDir = CodeFileSaverExecutor.executeCodeSave(parseResult, codeGenType, appId);
                        log.info("保存成功，路径：{}", saveDir.getAbsolutePath());
                    } catch (Exception e) {
                        log.error("保存失败，{}",e.getMessage());
                    }
                });
    }
}