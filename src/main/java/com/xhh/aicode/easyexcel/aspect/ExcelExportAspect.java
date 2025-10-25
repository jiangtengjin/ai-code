package com.xhh.aicode.easyexcel.aspect;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.mybatisflex.core.paginate.Page;
import com.xhh.aicode.common.BaseResponse;
import com.xhh.aicode.easyexcel.annotation.ExcelExport;
import com.xhh.aicode.easyexcel.constant.EasyExcelConstant;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel 导出切面
 */
@Slf4j
@Aspect
@Component
public class ExcelExportAspect {

    @AfterReturning(value = "@annotation(excelExport)", returning = "result")
    public void exportExcel(ProceedingJoinPoint point, ExcelExport excelExport, Object result) throws Throwable {
        // 获取返回值
        List<?> data = extraData(result);
        if (data.isEmpty()) {
            log.error("没有数据可以导出");
        }
        String fileSavePath = EasyExcelConstant.EASY_EXCEL_ROOT_DIR + File.separator + excelExport.fileName() + ".csv";
        log.info("开始文件导出，文件名：{}", excelExport.fileName());
        // 如果这里想使用 CSV 则 传入excelType参数即可
        EasyExcel.write(fileSavePath, excelExport.clazz())
                .excelType(ExcelTypeEnum.CSV)
                .sheet(excelExport.sheetName())
                .doWrite(data);
        log.info("文件导出完成，{} -> {}", excelExport.fileName(), fileSavePath);
    }

    /**
     * 从 BaseResponse 中提取出分页列表
     *
     * @param result
     * @return
     */
    private List<?> extraData(Object result) {
        if (result instanceof BaseResponse<?>) {
            Page data = (Page) ((BaseResponse<?>) result).getData();
            if (ObjectUtil.isEmpty(data)) {
                return new ArrayList<>(0);
            }
            return (List<?>) data.getRecords();
        }
        return new ArrayList<>(0);
    }

}
