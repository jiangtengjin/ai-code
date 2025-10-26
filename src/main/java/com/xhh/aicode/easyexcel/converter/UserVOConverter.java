package com.xhh.aicode.easyexcel.converter;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.xhh.aicode.model.vo.UserVO;

/**
 * 定义文件导出转换器
 * 指定嵌套对象导出的转换规则 UserVO -> userName
 */
public class UserVOConverter implements Converter<UserVO> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return UserVO.class;
    }

    @Override
    public WriteCellData<?> convertToExcelData(UserVO value,
                                               ExcelContentProperty contentProperty,
                                               GlobalConfiguration globalConfiguration) throws Exception {
        if (ObjectUtil.isEmpty(value)) {
            return new WriteCellData<>("");
        }
        return new WriteCellData<>(value.getUserName());
    }
}