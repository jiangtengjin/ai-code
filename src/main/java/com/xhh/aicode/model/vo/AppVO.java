package com.xhh.aicode.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.xhh.aicode.easyexcel.converter.UserVOConverter;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 应用视图对象
 *
 * @author <a href="https://github.com/jiangtengjin">xhh</a>
 */
@Data
public class AppVO implements Serializable {

    /**
     * id
     */
    @ExcelProperty(value = "id", index = 0)
    private Long id;

    /**
     * 应用名称
     */
    @ExcelProperty(value = "应用名称", index = 1)
    private String appName;

    /**
     * 应用封面
     */
    @ExcelProperty(value = "应用封面", index = 2)
    private String cover;

    /**
     * 应用初始化的 prompt
     */
    @ExcelProperty(value = "应用初始化的 prompt", index = 3)
    private String initPrompt;

    /**
     * 代码生成类型（枚举）
     */
    @ExcelProperty(value = "码生成类型", index = 4)
    private String codeGenType;

    /**
     * 部署标识
     */
    private String deployKey;

    /**
     * 部署时间
     */
    @ExcelProperty(value = "部署时间", index = 6)
    private LocalDateTime deployedTime;

    /**
     * 优先级
     */
    @ExcelProperty(value = "优先级", index = 5)
    private Integer priority;

    /**
     * 创建用户id
     */
    private Long userId;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间", index = 8)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建用户信息
     */
    @ExcelProperty(value = "创建者", index = 7, converter = UserVOConverter.class)
    private UserVO user;

    private static final long serialVersionUID = 1L;
}
