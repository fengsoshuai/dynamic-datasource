package org.feng.controller;

import org.feng.datasource.DataSourceConstant;
import org.feng.datasource.ExchangeDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO 类的描述
 *
 * @version v1.0
 * @author: fengjinsong
 * @date: 2022年05月05日 15时43分
 */
@RestController
public class TestController {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/test")
    public String test() {
        Map<String, Object> map = new HashMap<>(16);

        return testMaster().toString() + "\n" + testSlave1();
    }

    @ExchangeDataSource
    public Map<String, Object> testMaster() {
        return jdbcTemplate.queryForMap("select * from test");
    }

    @ExchangeDataSource(DataSourceConstant.SLAVE1)
    public Map<String, Object> testSlave1() {
        return jdbcTemplate.queryForMap("select * from test");
    }
}
