package com.kms.chatters.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ConfigurationProperties
@EnableTransactionManagement
public class DataSourceConfig {

    // @Bean(name = "dataSource")
    // public DataSource DataSource() {
    //     return DataSourceBuilder.create().type(HikariDataSource.class).build();
    // }

    @Autowired
    private DataSource dataSource;
    
    @Bean
    public PlatformTransactionManager transactionManager() throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }
}