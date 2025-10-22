package com.xhh.aicode.langgraph4j.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import com.xhh.aicode.exception.BusinessException;
import com.xhh.aicode.exception.ErrorCode;
import com.xhh.aicode.langgraph4j.model.ImageResource;
import com.xhh.aicode.langgraph4j.model.enums.ImageCategoryEnum;
import com.xhh.aicode.manager.CosManager;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class MermaidDiagramTool {

    @Resource
    private CosManager cosManager;

    @Tool("将 Mermaid 代码转换为架构图图片，用于展示系统结构和技术关系")
    public List<ImageResource> generateMermaidDiagram(@P("Mermaid 图表代码") String mermaidCode,
                                                      @P("架构图描述") String description) {
        if (StrUtil.isBlank(mermaidCode)) {
            return new ArrayList<>();
        }
        try {
            // 转换为SVG图片
            File diagramFile = convertMermaidToSvgWithSpecifiedChromePath(mermaidCode);
            // 上传到COS
            String keyName = String.format("/mermaid/%s/%s",
                    RandomUtil.randomString(5), diagramFile.getName());
            String cosUrl = cosManager.uploadFile(keyName, diagramFile);
            // 清理临时文件
            FileUtil.del(diagramFile);
            if (StrUtil.isNotBlank(cosUrl)) {
                return Collections.singletonList(ImageResource.builder()
                        .category(ImageCategoryEnum.ARCHITECTURE)
                        .description(description)
                        .url(cosUrl)
                        .build());
            }
        } catch (Exception e) {
            log.error("生成架构图失败: {}", e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    /**
     * 将Mermaid代码转换为SVG图片
     */
    private File convertMermaidToSvg(String mermaidCode) {
        // 创建临时输入文件
        File tempInputFile = FileUtil.createTempFile("mermaid_input_", ".mmd", true);
        FileUtil.writeUtf8String(mermaidCode, tempInputFile);
        // 创建临时输出文件
        File tempOutputFile = FileUtil.createTempFile("mermaid_output_", ".svg", true);
        // 根据操作系统选择命令
        String command = SystemUtil.getOsInfo().isWindows() ? "mmdc.cmd" : "mmdc";
        // 构建命令
        String cmdLine = String.format("%s -i %s -o %s -b transparent",
                command,
                tempInputFile.getAbsolutePath(),
                tempOutputFile.getAbsolutePath()
        );
        // 执行命令
        RuntimeUtil.execForStr(cmdLine);
        // 检查输出文件
        if (!tempOutputFile.exists() || tempOutputFile.length() == 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Mermaid CLI 执行失败");
        }
        // 清理输入文件，保留输出文件供上传使用
        FileUtil.del(tempInputFile);
        return tempOutputFile;
    }

    /**
     * 将Mermaid代码转换为SVG图片, 指定 chromePath 路径
     * 针对错误：
     * 2025-09-20 14:06:18.230 INFO 3672 --- [ main] c.a.i.l.tools.MermaidDiagramTool : Mermaid CLI 输出:
     * Error: Could not find expected browser (chrome) locally. Run `npm install` to download the correct Chromium revision (1045629).
     * at ChromeLauncher.launch (file:///G:/jetbrains_tools/frontend/Nvm/node_global/node_modules/@mermaid-js/mermaid-cli/node_modules/puppeteer-core/lib/esm/puppeteer/node/ChromeLauncher.js:64:23)
     * at async run (file:///G:/jetbrains_tools/frontend/Nvm/node_global/node_modules/@mermaid-js/mermaid-cli/src/index.js:343:19)
     * at async cli (file:///G:/jetbrains_tools/frontend/Nvm/node_global/node_modules/@mermaid-js/mermaid-cli/src/index.js:138:3)
     * 2025-09-20 14:06:18.235 ERROR 3672 --- [ main] c.a.i.l.tools.MermaidDiagramTool : 生成架构图失败: Mermaid CLI 执行失败
     * @param mermaidCode mermaid代码
     * @return SVG图片文件
     */
    private File convertMermaidToSvgWithSpecifiedChromePath(String mermaidCode) {
        File tempInputFile = FileUtil.createTempFile("mermaid_input_", ".mmd", true);
        FileUtil.writeUtf8String(mermaidCode, tempInputFile);

        File tempOutputFile = FileUtil.createTempFile("mermaid_output_", ".svg", true);
        String command = SystemUtil.getOsInfo().isWindows() ? "mmdc.cmd" : "mmdc";
        String chromePath = "C:\\Users\\机hui难得\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe";

        try {
            // 用 ProcessBuilder 设置环境变量
            ProcessBuilder pb = new ProcessBuilder(
                    command,
                    "-i", tempInputFile.getAbsolutePath(),
                    "-o", tempOutputFile.getAbsolutePath(),
                    "-b", "transparent"
            );

            // 加环境变量 Puppeteer 执行路径
            pb.environment().put("PUPPETEER_EXECUTABLE_PATH", chromePath);

            pb.redirectErrorStream(true);
            Process process = pb.start();

            // 读取输出日志
            String result = IoUtil.read(process.getInputStream(), Charset.defaultCharset());
            log.info("Mermaid CLI 输出: {}", result);

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Mermaid CLI 执行失败，exit=" + exitCode);
            }

            if (!tempOutputFile.exists() || tempOutputFile.length() == 0) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Mermaid CLI 没有生成输出文件");
            }
            return tempOutputFile;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成架构图失败: " + e.getMessage());
        } finally {
            FileUtil.del(tempInputFile);
        }

    }}

