package com.xhh.aicode.easyexcel.service;

import java.util.List;

public interface EasyExcelService {

    /**
     * 导出 excel
     *
     * @param fileName      文件名
     * @param sheetName     sheet名，默认是 sheet1
     * @param data          需要导出的数据
     * @param clazz         数据类型
     * @return              文件访问路径
     */
    String export(String fileName, String sheetName, List<?> data, Class<?> clazz);

}
