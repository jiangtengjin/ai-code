package com.xhh.aicode.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserVO implements Serializable {

    /**
     * id
     */
    @ExcelProperty("id")
    private Long id;
    
    /**
     * 账号
     */
    @ExcelProperty("账号")
    private String userAccount;

    /**
     * 用户昵称
     */
    @ExcelProperty("用户昵称")
    private String userName;

    /**
     * 用户头像
     */
    @ExcelProperty("用户头像")
    private String userAvatar;

    /**
     * 用户简介
     */
    @ExcelProperty("用户简介")
    private String userProfile;

    /**
     * 用户角色：user/admin
     */
    @ExcelProperty("用户角色")
    private String userRole;

    /**
     * 创建时间
     */
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    private static final long serialVersionUID = 1L;
}
