package com.xhh.aicode.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.xhh.aicode.model.dto.app.AppQueryRequest;
import com.xhh.aicode.model.entity.App;
import com.xhh.aicode.model.vo.AppVO;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author <a href="https://github.com/jiangtengjin">xhh</a>
 */
public interface AppService extends IService<App> {

    /**
     * 获取应用信息（脱敏后）
     *
     * @param app   应用信息
     * @return      脱敏后的结果
     */
    AppVO getAppVO(App app);

    List<AppVO> getAppVOList(List<App> appList);

    /**
     * 构造分页查询的 queryWrapper 对象
     *
     * @param appQueryRequest   分页请求对象
     * @return                  分页查询的 queryWrapper 对象
     */
    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);
}
