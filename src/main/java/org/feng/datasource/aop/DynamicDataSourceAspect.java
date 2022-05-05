package org.feng.datasource.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.feng.datasource.DynamicDataSourceContextHolder;
import org.feng.datasource.ExchangeDataSource;
import org.springframework.stereotype.Component;

/**
 * 切面实现动态数据源切换
 *
 * @version v1.0
 * @author: fengjinsong
 * @date: 2022年05月05日 16时35分
 */
@Aspect
@Component
public class DynamicDataSourceAspect {
    @Pointcut("@annotation(org.feng.datasource.ExchangeDataSource)")
    public void dataSourcePointCut(){}

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        String merchantKey = parseMethodWithExchangeDataSource(joinPoint).value();
        DynamicDataSourceContextHolder.setDataSourceKey(merchantKey);
        try {
            // 执行方法
            return joinPoint.proceed();
        } finally {
            // 恢复数据源
            DynamicDataSourceContextHolder.removeDataSourceKey();
        }
    }

    private ExchangeDataSource parseMethodWithExchangeDataSource(ProceedingJoinPoint joinPoint) {
        Class<?> targetClass = joinPoint.getTarget().getClass();
        // 如果类上有切换数据源的注解
        if(targetClass.isAnnotationPresent(ExchangeDataSource.class)){
            return targetClass.getAnnotation(ExchangeDataSource.class);
        }

        // 获取方法上的注解
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        return signature.getMethod().getAnnotation(ExchangeDataSource.class);
    }
}
