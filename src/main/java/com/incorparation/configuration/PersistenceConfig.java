package com.incorparation.configuration;

import com.incorparation.util.KeyManager;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.incorparation.dao"})
@RequiredArgsConstructor
public class PersistenceConfig {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    private final KeyManager keyManager;

    @Bean(destroyMethod = "close")
    DataSource dataSource() {
        var hikariConfig = new HikariConfig();

        hikariConfig.setDriverClassName(JDBC_DRIVER);
        hikariConfig.setJdbcUrl(keyManager.getDbUrl());
        hikariConfig.setUsername(keyManager.getUserName());
        hikariConfig.setPassword(keyManager.getPassword());
        hikariConfig.setMaxLifetime(keyManager.getMaxLifeTime());
        hikariConfig.setMaximumPoolSize(keyManager.getMaxPollSize());
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setPoolName("springHikariCP");

        return new HikariDataSource(hikariConfig);
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        var entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setPackagesToScan("com.incorparation.model");

        entityManagerFactoryBean.setJpaProperties(getJpaProperties());

        return entityManagerFactoryBean;
    }

    @Bean
    JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        var transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    private Properties getJpaProperties() {
        var jpaProperties = new Properties();

        jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        jpaProperties.setProperty("hibernate.show_sql", "true");
        jpaProperties.setProperty("hibernate.format_sql", "true");
        return jpaProperties;
    }
}