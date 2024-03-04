package com.jbl.t24.rest.api.config.MultipleDbConfiguration.RtgsDbConfiguration;

import java.util.HashMap;
import java.util.Objects;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;



@Configuration
//@EnableJpaAuditing
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.jbl.t24.rest.api.repository.rtgs", 
    entityManagerFactoryRef = "rtgsEntityManagerFactory", 
    repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class,
    transactionManagerRef = "rtgsTransactionManager")
public class RtgsJpaConfiguration {

    @Bean(name = "rtgsDataSource")
    @ConfigurationProperties("spring.datasource.rtgs")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

 
    @Bean(name = "rtgsEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean rtgsEntityManagerFactory(
            @Qualifier("rtgsDataSource") DataSource dataSource,
            EntityManagerFactoryBuilder builder) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        // properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.listeners.envers.rev_entity", "com.jbl.audit.AuditRevisionEntity");
        return builder
                .dataSource(dataSource)
                .properties(properties)
                .packages("com.jbl.t24.rest.api.model.rtgs", "com.jbl.t24.rest.api.audit", "com.jbl.t24.rest.api.model.ApiLogs")
                .persistenceUnit("rtgs")
                .build();
    }

   
    @Bean(name = "rtgsTransactionManager")
    public PlatformTransactionManager rtgsTransactionManager(
            @Qualifier("rtgsEntityManagerFactory") EntityManagerFactory rtgsTransactionManager) {
        return new JpaTransactionManager(rtgsTransactionManager);
    }
}
