package com.xhh.aicode.Captcha.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ImageCodeCheckRequest implements Serializable {

    /**
     * UUID
     */
    private String uuid;

    /**
     * 图片验证码
     */
    private String imageCode;

    private static final long serialVersionUID = 1L;
}
