package com.xhh.aicode.easyexcel.aspect;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.mybatisflex.core.paginate.Page;
import com.xhh.aicode.common.BaseResponse;
import com.xhh.aicode.easyexcel.annotation.ExcelColumn;
import com.xhh.aicode.easyexcel.annotation.ExcelExport;
import com.xhh.aicode.easyexcel.classgenerator.DynamicExcelClassGenerator;
import com.xhh.aicode.easyexcel.constant.EasyExcelConstant;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel 导出切面
 */
@Slf4j
@Aspect
@Component
public class ExcelExportAspect {

    @Resource
    private DynamicExcelClassGenerator classGenerator;

    @AfterReturning(value = "@annotation(excelExport)", returning = "result")
    public void exportExcel(ProceedingJoinPoint point, ExcelExport excelExport, Object result) throws Throwable {
        // 获取返回值
        List<?> data = extraData(result);
        if (data.isEmpty()) {
            log.error("没有数据可以导出");
        }
        // 生成动态类（只包含有 ExcelColumn 注解的字段）
        Class<?> dynamicClass = classGenerator.generateDynamicClass(excelExport.clazz());
        // 转换数据到动态类实例
        List<?> convertedData = convertDataToDynamicClass(data, dynamicClass, excelExport.clazz());
        String fileSavePath = EasyExcelConstant.EASY_EXCEL_ROOT_DIR + File.separator + excelExport.fileName() + ".csv";
        log.info("开始文件导出，文件名：{}", excelExport.fileName());
        // 如果这里想使用 CSV 则 传入excelType参数即可
        EasyExcel.write(fileSavePath, excelExport.clazz())
                .excelType(ExcelTypeEnum.CSV)
                .sheet(excelExport.sheetName())
                .doWrite(convertedData);
        log.info("文件导出完成，{} -> {}", excelExport.fileName(), fileSavePath);
    }

    /**
     * 转换数据到动态类实例
     */
    private List<Object> convertDataToDynamicClass(List<?> originalData, Class<?> dynamicClass, Class<?> originalClass) throws Exception {
        List<Object> convertedData = new ArrayList<>();

        for (Object original : originalData) {
            Object dynamicInstance = dynamicClass.newInstance();
            copyMatchingFields(original, dynamicInstance, originalClass);
            convertedData.add(dynamicInstance);
        }

        return convertedData;
    }

    /**
     * 复制匹配的字段值
     */
    private void copyMatchingFields(Object source, Object target, Class<?> sourceClass) throws Exception {
        Field[] sourceFields = sourceClass.getDeclaredFields();
        Field[] targetFields = target.getClass().getDeclaredFields();

        for (Field sourceField : sourceFields) {
            ExcelColumn excelColumn = sourceField.getAnnotation(ExcelColumn.class);
            if (excelColumn == null || excelColumn.ignore()) {
                continue;
            }

            for (Field targetField : targetFields) {
                if (sourceField.getName().equals(targetField.getName()) &&
                        sourceField.getType().equals(targetField.getType())) {

                    sourceField.setAccessible(true);
                    targetField.setAccessible(true);

                    Object value = sourceField.get(source);
                    targetField.set(target, value);
                    break;
                }
            }
        }
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
