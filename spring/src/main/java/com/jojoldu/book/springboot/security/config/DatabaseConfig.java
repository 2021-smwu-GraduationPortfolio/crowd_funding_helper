package com.jojoldu.book.springboot.security.config;


import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration

public class DatabaseConfig {
    @Bean
    public DataSource dataSource(){
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
        dataSourceBuilder.username("root");
        dataSourceBuilder.password("0000");
        dataSourceBuilder.url("jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC&autoReconnect=true");
        return dataSourceBuilder.build();
    }
}
