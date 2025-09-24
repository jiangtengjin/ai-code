package com.xhh.aicode.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xhh.aicode.model.entity.App;
import com.xhh.aicode.mapper.AppMapper;
import com.xhh.aicode.service.AppService;
import org.springframework.stereotype.Service;

/**
 * 应用 服务层实现。
 *
 * @author <a href="https://github.com/jiangtengjin">xhh</a>
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App>  implements AppService{

}
