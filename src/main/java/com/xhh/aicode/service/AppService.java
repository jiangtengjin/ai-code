package com.xhh.aicode.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.xhh.aicode.model.dto.app.AppAddRequest;
import com.xhh.aicode.model.dto.app.AppQueryRequest;
import com.xhh.aicode.model.entity.App;
import com.xhh.aicode.model.entity.User;
import com.xhh.aicode.model.vo.AppVO;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author <a href="https://github.com/jiangtengjin">xhh</a>
 */
public interface AppService extends IService<App> {

    /**
     * 通过对话生成应用
     *
     * @param appId     应用 id
     * @param message   用户提示词
     * @param loginUser 登录用户
     * @return          响应流
     */
    Flux<String> chatToGenCode(Long appId, String message, User loginUser);

    /**
     * 创建应用
     *
     * @param appAddRequest   应用信息
     * @param loginUser       登录用户
     * @return                应用 id
     */
    Long createApp(AppAddRequest appAddRequest, User loginUser);

    /**
     * 部署服务
     *
     * @param appId         应用 id
     * @param loginUser     当前登录用户
     * @return              服务链接
     */
    String deployApp(Long appId, User loginUser);

    /**
     * 异步生成应用截图并更新封面
     *
     * @param appId  应用ID
     * @param appUrl 应用访问URL
     */
    void generateAppScreenshotAsync(Long appId, String appUrl);

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

    /**
     * 删除应用关联的本地资源目录（生成输出与部署目录）
     * 失败仅记录日志，不抛出异常
     *
     * @param app 应用实体
     */
    void deleteAppResources(App app);
}
