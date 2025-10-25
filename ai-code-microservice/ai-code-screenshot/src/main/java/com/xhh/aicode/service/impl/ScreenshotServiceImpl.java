package com.xhh.aicode.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.xhh.aicode.exception.ErrorCode;
import com.xhh.aicode.exception.ThrowUtils;
import com.xhh.aicode.manager.CosManager;
import com.xhh.aicode.service.ScreenshotService;
import com.xhh.aicode.utils.WebScreenshotUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Slf4j
public class ScreenshotServiceImpl implements ScreenshotService {

    @Resource
    private CosManager cosManager;

    @Override
    public String generateAndUploadScreenshot(String webUrl) {
        ThrowUtils.throwIf(StrUtil.isBlank(webUrl), ErrorCode.PARAMS_ERROR, "网页 URL 不能为空");
        log.info("开始生成网页截图，url：{}", webUrl);
        // 生成本地截图
        String localScreenshotPath = WebScreenshotUtils.saveWebScreenshot(webUrl);
        ThrowUtils.throwIf(StrUtil.isBlank(localScreenshotPath), ErrorCode.OPERATION_ERROR, "本地截图生成失败");
        // 上传到对象存储
        try {
            String cosUrl = uploadScreenshotToCos(localScreenshotPath);
            ThrowUtils.throwIf(StrUtil.isBlank(cosUrl), ErrorCode.OPERATION_ERROR, "截图上传 COS 失败");
            log.info("网页截图生成并上传成功：{} -> {}", webUrl, cosUrl);
            return cosUrl;
        } finally {
            // 清理文件
            cleanupLocalFile(localScreenshotPath);
        }

    }


    /**
     * 上传截图到对象存储
     *
     * @param localScreenshotPath   本地截图路径
     * @return                      对象存储访问 URL,失败返回 null
     */
    private String uploadScreenshotToCos(String localScreenshotPath) {
        if (StrUtil.isBlank(localScreenshotPath)) {
            return null;
        }
        File screenshotFile = new File(localScreenshotPath);
        if (!screenshotFile.exists()) {
            log.error("截图文件不存在：{}", localScreenshotPath);
            return null;
        }
        // 生成 COS 对象键
        String fileName = UUID.randomUUID().toString().substring(0, 8) + "_compressed.jpg";
        String cosKey = generateScreenshotKey(fileName);
        return cosManager.uploadFile(cosKey, screenshotFile);
    }

    /**
     * 清理本地文件
     *
     * @param localFilePath   需要清理文件的路径
     */
    private void cleanupLocalFile(String localFilePath) {
        File localFile = new File(localFilePath);
        if (localFile.exists()) {
            File parentDir = localFile.getParentFile();
            FileUtil.del(parentDir);
            log.info("本地截图文件清理成功：{}", localFilePath);
        }
    }

    /**
     * 生成截图的对象存储 key
     * 格式：/screenshots/2025/10/12/fileName,jpg
     * @param fileName       文件名
     * @return               对象存储 key
     */
    private String generateScreenshotKey(String fileName) {
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return String.format("screenshots/%s/%s", datePath, fileName);
    }
}
