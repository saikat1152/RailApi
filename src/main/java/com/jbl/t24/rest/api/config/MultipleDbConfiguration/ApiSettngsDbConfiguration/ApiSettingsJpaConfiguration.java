package com.jbl.t24.rest.api.config.MultipleDbConfiguration.ApiSettngsDbConfiguration;

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
import org.springframework.context.annotation.Primary;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;



@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.jbl.t24.rest.api.repository.base", 
    entityManagerFactoryRef = "baseEntityManagerFactory", 
    repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class,
    transactionManagerRef = "baseTransactionManager")
public class ApiSettingsJpaConfiguration {

	@Primary
    @Bean(name = "baseDataSource")
    @ConfigurationProperties("spring.datasource.base")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

	@Primary
    @Bean(name = "baseEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean baseEntityManagerFactory(
            @Qualifier("baseDataSource") DataSource dataSource,
            EntityManagerFactoryBuilder builder) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        // properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.listeners.envers.rev_entity", "com.jbl.audit.AuditRevisionEntity");
        return builder
                .dataSource(dataSource)
                .properties(properties)
                .packages("com.jbl.t24.rest.api.model.base", "com.jbl.t24.rest.api.audit")
                .persistenceUnit("base")
                .build();
    }

	@Primary
    @Bean(name = "baseTransactionManager")
    public PlatformTransactionManager baseTransactionManager(
            @Qualifier("baseEntityManagerFactory") EntityManagerFactory baseTransactionManager) {
        return new JpaTransactionManager(baseTransactionManager);
    }

}
