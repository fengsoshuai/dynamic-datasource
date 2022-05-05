package org.feng.datasource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解切换数据源
 *
 * @version v1.0
 * @author: fengjinsong
 * @date: 2022年05月05日 16时33分
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExchangeDataSource {
    /**
     * 数据源名称：租户key
     *
     * @return 数据源名称
     */
    String value() default DataSourceConstant.MASTER;
}
