package com.opsmarttech.toyota.storeflow.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean(name = "gp")
    @Primary
    @ConfigurationProperties(prefix = "gp.datasource") // gp 数据库
    public DataSource dataSourceGp() {
        return DataSourceBuilder.create().build();
    }


    @Bean(name = "mysql")
    @ConfigurationProperties(prefix = "rds.datasource") // mysql 数据库
    public DataSource dataSourceMysql() {
        return DataSourceBuilder.create().build();
    }

}

