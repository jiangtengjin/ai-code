package com.xhh.aicode.easyexcel.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.xhh.aicode.easyexcel.constant.EasyExcelConstant;
import com.xhh.aicode.easyexcel.service.EasyExcelService;
import com.xhh.aicode.exception.BusinessException;
import com.xhh.aicode.exception.ErrorCode;
import com.xhh.aicode.exception.ThrowUtils;
import com.xhh.aicode.manager.CosManager;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class EasyExcelServiceImpl implements EasyExcelService {

    @Resource
    private CosManager cosManager;

    @Override
    public String export(String fileName, String sheetName, List<?> data, Class<?> clazz) {
        return exportExcludeColumn(fileName, sheetName, data, clazz, null);
    }

    @Override
    public String exportExcludeColumn(String fileName, String sheetName, List<?> data, Class<?> clazz, Set<String> excludeFields) {
        // 参数校验
        ThrowUtils.throwIf(data.isEmpty(), ErrorCode.PARAMS_ERROR, "没有数据可以导出");
        if (StrUtil.isBlank(fileName)) {
            fileName = UUID.randomUUID().toString().substring(0, 6);
        }
        if (StrUtil.isBlank(sheetName)) {
            sheetName = EasyExcelConstant.DEFAULT_SHEET_NAME;
        }
        File excel = null;
        try {
            // 写入 excel
            log.info("开始文件导出，文件名：{}", fileName);
            String fileSavePath = EasyExcelConstant.EASY_EXCEL_ROOT_DIR + File.separator + fileName + ".csv";
            // 如果这里想使用 CSV 则 传入excelType参数即可
            try {
                // 导出全部的列
                if (CollUtil.isEmpty(excludeFields) || excludeFields.isEmpty()) {
                    EasyExcel.write(fileSavePath, clazz)
                            .excelType(ExcelTypeEnum.CSV)
                            .sheet(sheetName)
                            .doWrite(data);
                } else {
                    // 排除指定的列
                    EasyExcel.write(fileSavePath, clazz)
                            .excelType(ExcelTypeEnum.CSV)
                            .sheet(sheetName)
                            .excludeColumnFieldNames(excludeFields)
                            .doWrite(data);
                }
            } catch (Exception e) {
                log.error("写入文件失败，{}", e.getMessage());
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "写入文件失败");
            }
            // 上传 COS 对象存储
            excel = new File(fileSavePath);
            if (!excel.exists() ) {
                log.error("文件不存在，请检查文件路径：{}", fileSavePath);
            }
            String uploadPath = String.format("export/excel/%s/%s%s",
                    DateUtil.formatDate(new Date()), fileName, ".csv");;
            // 返回可访问地址
            return cosManager.uploadFile(uploadPath, excel);
        } finally {
            log.info("清理本地文件，{}", excel.getPath());
            // 清理临时文件
            FileUtil.del(excel);
        }
    }

}
