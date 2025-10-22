package com.xhh.aicode.langgraph4j.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesis;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisParam;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisResult;
import com.xhh.aicode.langgraph4j.model.ImageResource;
import com.xhh.aicode.langgraph4j.model.enums.ImageCategoryEnum;
import com.xhh.aicode.manager.CosManager;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class LogoGeneratorTool {

    @Value("${dashscope.api-key:}")
    private String dashScopeApiKey;

    @Value("${dashscope.image-model:wan2.2-t2i-flash}")
    private String imageModel;

    @Resource
    private CosManager cosManager;

    @Tool("根据描述生成 Logo 设计图片，用于网站品牌标识")
    public List<ImageResource> generateLogos(@P("Logo 设计描述，如名称、行业、风格等，尽量详细") String description) {
        List<ImageResource> logoList = new ArrayList<>();
        try {
            // 构建 Logo 设计提示词
            String logoPrompt = String.format("生成 Logo，Logo 中禁止包含任何文字！Logo 介绍：%s", description);
            ImageSynthesisParam param = ImageSynthesisParam.builder()
                    .apiKey(dashScopeApiKey)
                    .model(imageModel)
                    .prompt(logoPrompt)
                    .size("512*512")
                    .n(1) // 生成 1 张足够，因为 AI 不知道哪张最好
                    .build();
            ImageSynthesis imageSynthesis = new ImageSynthesis();
            ImageSynthesisResult result = imageSynthesis.call(param);
            if (result != null && result.getOutput() != null && result.getOutput().getResults() != null) {
                List<Map<String, String>> results = result.getOutput().getResults();
                for (Map<String, String> imageResult : results) {
                    String imageUrl = imageResult.get("url");
                    if (StrUtil.isNotBlank(imageUrl)) {
                        String cosUrl = persistLogoToCos(imageUrl);
                        if (StrUtil.isNotBlank(cosUrl)) {
                            logoList.add(ImageResource.builder()
                                    .category(ImageCategoryEnum.LOGO)
                                    .description(description)
                                    .url(cosUrl)
                                    .build());
                        } else {
                            log.error("Logo 持久化到 COS 失败，源地址：{}", imageUrl);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("生成 Logo 失败: {}", e.getMessage(), e);
        }
        return logoList;
    }

    /**
     * 将远端临时 Logo 图片下载到本地 tmp 并上传到 COS，返回持久化 URL
     * @param imageUrl OSS 临时地址
     * @return COS 持久化 URL，失败返回 null
     */
    private String persistLogoToCos(String imageUrl) {
        File tmpDir = new File("tmp");
        FileUtil.mkdir(tmpDir);

        String ext = StrUtil.subAfter(imageUrl, ".", true);
        if (StrUtil.isBlank(ext) || ext.length() > 4) {
            ext = "png";
        }
        String fileName = "logo_" + RandomUtil.randomString(8) + "." + ext;
        File tmpFile = new File(tmpDir, fileName);

        try {
            HttpUtil.downloadFile(imageUrl, tmpFile);
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String cosKey = String.format("logo/%s/%s", datePath, fileName);
            String cosUrl = cosManager.uploadFile(cosKey, tmpFile);
            return StrUtil.isNotBlank(cosUrl) ? cosUrl : null;
        } catch (Exception ex) {
            log.error("Logo 下载/上传失败：{}，错误：{}", imageUrl, ex.getMessage(), ex);
            return null;
        } finally {
            // 清理临时文件
            FileUtil.del(tmpFile);
        }
    }
}
