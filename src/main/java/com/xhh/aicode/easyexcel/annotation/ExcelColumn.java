package com.xhh.aicode.easyexcel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {

    /** 列名 */
    String[] value() default {};

    /** 列索引 */
    int index() default -1;

    /** 日期格式 */
    String dateFormat() default "";

    /** 数字格式 */
    String numberFormat() default "";

    /** 列宽 */
    int width() default -1;

    /** 是否忽略该字段 */
    boolean ignore() default false;

}
