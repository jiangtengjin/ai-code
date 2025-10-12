package com.xhh.aicode.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WebScreenshotUtilsTest {

    @Test
    void saveWebScreenshot() {
        String testUrl = "https://www.baidu.com";
        String webPageScreenshot = WebScreenshotUtils.saveWebScreenshot(testUrl);
        Assertions.assertNotNull(webPageScreenshot);
    }
}