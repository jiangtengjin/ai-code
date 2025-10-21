package com.xhh.aicode.manager;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.xhh.aicode.config.CosClientConfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * COS 对象存储管理器
 */
@Service
@Slf4j
@ConditionalOnBean(COSClient.class)
public class CosManager {

    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private COSClient cosClient;

    /**
     * 上传对象
     *
     * @param key       唯一键
     * @param file      需要上传的文件
     * @return          上传结果
     */
    public PutObjectResult putObject(String key, File file) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key, file);
        return cosClient.putObject(putObjectRequest);
    }

    /**
     * 上传文件到 COS 并返回访问 URL
     * @param key       唯一键
     * @param file      需要上传的文件
     * @return          访问 URL
     */
    public String uploadFile(String key, File file) {
        PutObjectResult result = putObject(key, file);
        if (result != null) {
            // 构建访问 URL
            String url = String.format("%s/%s", cosClientConfig.getHost(), key);
            log.info("文件上传 COS 成功：{} -> {}", file.getName(), url);
            return url;
        }
        log.error("文件上传 COS 失败，返回结果为空");
        return null;
    }

    /**
     * 删除对象
     * @param key   对象存储 key
     */
    public void deleteObject(String key) {
        cosClient.deleteObject(cosClientConfig.getBucket(), key);
    }

}
