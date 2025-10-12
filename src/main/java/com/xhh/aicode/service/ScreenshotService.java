package com.xhh.aicode.service;

public interface ScreenshotService {

    /**
     * 生成并上传网页截图
     *
     * @param webUrl    网页地址
     * @return          截图可访问的 URL
     */
    String generateAndUploadScreenshot(String webUrl);

}
