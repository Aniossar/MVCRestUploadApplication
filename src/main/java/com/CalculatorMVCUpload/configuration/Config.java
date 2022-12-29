package com.CalculatorMVCUpload.configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

@Configuration
@ComponentScan
@EnableTransactionManagement
@Log
public class Config implements WebMvcConfigurer {

    @Value("${postgres.datasource.url}")
    private String databaseUrl;

    @Value("${postgres.datasource.username}")
    private String databaseLogin;

    @Value("${postgres.datasource.password}")
    private String databasePassword;

    @Bean
    public DataSource dataSource() {

        log.info("POSTGRES_URL in System.getEnv= " + System.getenv("POSTGRES_URL"));
        log.info("POSTGRES_URL in the class= " + databaseUrl);

        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass("org.postgresql.Driver");
            dataSource.setJdbcUrl(databaseUrl);
            dataSource.setUser(databaseLogin);
            dataSource.setPassword(databasePassword);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

        return transactionManager;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(false);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.CalculatorMVCUpload.entity");
        factory.setDataSource(dataSource());
        return factory;
    }

}
