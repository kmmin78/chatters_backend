package com.kms.chatters.config;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

    @Bean(name = "dataSorce")
    public DataSource DataSource() {
        return DataSourceBuilder.create().build();
    }
}