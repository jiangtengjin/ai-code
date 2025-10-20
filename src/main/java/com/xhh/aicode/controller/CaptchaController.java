package com.xhh.aicode.controller;

import cn.hutool.core.codec.Base64;
import com.google.code.kaptcha.Producer;
import com.xhh.aicode.Captcha.constant.CaptchaConstant;
import com.xhh.aicode.Captcha.model.vo.ImageCodeVO;
import com.xhh.aicode.common.BaseResponse;
import com.xhh.aicode.common.ResultUtils;
import com.xhh.aicode.rateLimiter.annotation.RateLimit;
import com.xhh.aicode.rateLimiter.enums.RateLimitType;
import com.xhh.aicode.utils.RedisCacheUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/captcha/image")
public class CaptchaController {

    @Resource
    private Producer captchaProducer;

    @Resource
    private RedisCacheUtil redisCacheUtil;

    @RateLimit(limitType = RateLimitType.USER, rate = 5, rateInterval = 60, message = "获取验证码过于频繁，请稍后再试")
    @GetMapping("/code")
    public BaseResponse<ImageCodeVO> getCode() {
        log.info("获取验证码");
        // 保存验证码信息,生成UUID并构建验证码键名
        String uuid = UUID.randomUUID().toString();
        String verifyKey = String.format("%s:%s", CaptchaConstant.CAPTCHA_CODE_KEY_PREFIX, uuid);
        String capStr = null, code = null;
        BufferedImage image = null;
        //字符型验证码
        capStr = code = captchaProducer.createText();
        image = captchaProducer.createImage(capStr);
        //将生成的验证码存入Redis缓存，并设置过期时间
        redisCacheUtil.setCacheObject(verifyKey, code, 2, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //将验证码图片转换为Base64编码格式,将UUID和Base64编码后的图片添加到响应对象并返回
        ImageCodeVO imageCodeVO = ImageCodeVO.builder()
                .uuid(uuid)
                .img(Base64.encode(os.toByteArray()))
                .build();
        return ResultUtils.success(imageCodeVO);
    }
}
