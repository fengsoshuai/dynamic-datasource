package org.feng.datasource.config;

import org.feng.datasource.DataSourceConstant;
import org.feng.datasource.DynamicDataSource;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源配置
 *
 * @version v1.0
 * @author: fengjinsong
 * @date: 2022年05月05日 14时56分
 */
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@PropertySource("classpath:config/jdbc.properties")
@Configuration
public class DynamicDataSourceConfig {


    /**
     * master 数据源
     *
     * @return 主要的数据源
     */
    @ConfigurationProperties(prefix = "spring.datasource.master")
    @Bean(DataSourceConstant.MASTER)
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * slave1 数据源
     *
     * @return slave1的数据源
     */
    @ConfigurationProperties(prefix = "spring.datasource.slave1")
    @Bean(DataSourceConstant.SLAVE1)
    public DataSource slave1DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean
    public DataSource dynamicDataSource() {
        Map<Object, Object> dataSourceMap = new HashMap<>(16);
        dataSourceMap.put(DataSourceConstant.MASTER, masterDataSource());
        dataSourceMap.put(DataSourceConstant.SLAVE1, slave1DataSource());

        // 设置动态数据源
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(dataSourceMap);
        dynamicDataSource.setDefaultTargetDataSource(masterDataSource());
        return dynamicDataSource;
    }

    @Primary
    @Bean
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(dynamicDataSource());
    }
}
