package com.xhh.aicode.model.dto.user;

import com.xhh.aicode.Captcha.model.dto.ImageCodeCheckRequest;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequest extends ImageCodeCheckRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;
}
