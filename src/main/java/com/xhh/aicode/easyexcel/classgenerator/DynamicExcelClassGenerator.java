package com.xhh.aicode.easyexcel.classgenerator;

import com.xhh.aicode.easyexcel.annotation.ExcelColumn;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.CtNewConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态实体类生成器
 */
@Slf4j
@Component
public class DynamicExcelClassGenerator {

    private static final Map<String, Class<?>> DYNAMIC_CLASS_CACHE = new ConcurrentHashMap<>();

    /**
     * 根据原始类和 ExcelColumn 注解生成动态类
     */
    public Class<?> generateDynamicClass(Class<?> originalClass) {
        String cacheKey = originalClass.getName();
        if (DYNAMIC_CLASS_CACHE.containsKey(cacheKey)) {
            return DYNAMIC_CLASS_CACHE.get(cacheKey);
        }

        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass dynamicClass = pool.makeClass(originalClass.getName() + "ExcelExport");

            // 获取所有带有 ExcelColumn 注解的字段
            List<Field> excelFields = getExcelColumnFields(originalClass);
            
            // 按 index 排序
            excelFields.sort(Comparator.comparingInt(field -> {
                ExcelColumn column = field.getAnnotation(ExcelColumn.class);
                return column.index() != -1 ? column.index() : Integer.MAX_VALUE;
            }));

            // 添加默认构造函数
            dynamicClass.addConstructor(CtNewConstructor.defaultConstructor(dynamicClass));

            Class<?> generatedClass = dynamicClass.toClass();
            DYNAMIC_CLASS_CACHE.put(cacheKey, generatedClass);
            
            return generatedClass;

        } catch (Exception e) {
            log.error("生成动态Excel类失败: {}", e.getMessage(), e);
            // 如果动态生成失败，返回原始类
            return originalClass;
        }
    }

    /**
     * 获取所有带有 ExcelColumn 注解的字段
     */
    private List<Field> getExcelColumnFields(Class<?> clazz) {
        List<Field> excelFields = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        
        for (Field field : fields) {
            ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
            if (excelColumn != null && !excelColumn.ignore()) {
                excelFields.add(field);
            }
        }
        return excelFields;
    }

}