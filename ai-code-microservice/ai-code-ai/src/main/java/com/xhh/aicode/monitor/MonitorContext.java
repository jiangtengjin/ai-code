package com.xhh.aicode.monitor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 监控器上下文对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonitorContext implements Serializable {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 应用id
     */
    private String appId;

    private static final long serialVersionUID = 1L;
}
