package com.xhh.aicode.easyexcel.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.xhh.aicode.easyexcel.constant.EasyExcelConstant;
import com.xhh.aicode.easyexcel.service.EasyExcelService;
import com.xhh.aicode.manager.CosManager;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class EasyExcelServiceImpl implements EasyExcelService {

    @Resource
    private CosManager cosManager;

    @Override
    public String export(String fileName, String sheetName, List<?> data, Class<?> clazz) {
        // 参数校验
        if (data.isEmpty()) {
            log.error("没有数据可以导出");
        }
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
            EasyExcel.write(fileSavePath, clazz)
                    .excelType(ExcelTypeEnum.CSV)
                    .sheet(sheetName)
                    .doWrite(data);
            // 上传 COS 对象存储
            excel = new File(fileSavePath);
            if (!excel.exists() ) {
                log.error("文件不存在，请检查文件路径：{}", fileSavePath);
            }
            String uploadPath = String.format("export/excel/%s/%s%s",
                    DateUtil.formatDate(new Date()), fileName, ".csv");;
            // 返回可访问地址
            String url = cosManager.uploadFile(uploadPath, excel);
            return url;
        } finally {
            log.info("清理本地文件，{}", excel.getPath());
            // 清理临时文件
            FileUtil.del(excel);
        }
    }
}
