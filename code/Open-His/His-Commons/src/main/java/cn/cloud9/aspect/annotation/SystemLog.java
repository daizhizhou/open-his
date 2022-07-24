package cn.cloud9.aspect.annotation;


import cn.cloud9.aspect.enums.BusinessType;
import cn.cloud9.aspect.enums.OperatorType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志标记注解
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLog {
    /**
     * 用于声明来源于哪个模块
     */
    String title() default "";

    /**
     * 用于声明操作人类别
     */
    OperatorType operatorType() default OperatorType.MANAGE;

    /**
     * 功能 用于声明业务类型
     */
    BusinessType businessType() default BusinessType.OTHER;

    /**
     * 是否保存请求的参数
     */
    boolean isSaveRequestData() default true;
}
