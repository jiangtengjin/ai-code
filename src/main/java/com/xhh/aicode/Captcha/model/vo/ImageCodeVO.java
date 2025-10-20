package com.xhh.aicode.Captcha.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
public class ImageCodeVO implements Serializable {

    /**
     * UUID
     */
    private String uuid;

    /**
     * 验证码图片
     */
    private String img;

    private static final long serialVersionUID = 1L;
}
