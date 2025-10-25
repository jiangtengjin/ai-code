package com.xhh.aicode.easyexcel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel 导出注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelExport {

    /** 文件名 */
    String fileName() default "excel";

    /** sheet 名 */
    String sheetName() default "Sheet1";

    Class<?> clazz() default void.class;

}
