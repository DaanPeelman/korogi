package com.korogi.core.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import com.korogi.core.persistence.EntityRepository;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@ComponentScan(basePackageClasses = EntityRepository.class)
public class CoreConfig {

    @Bean(initMethod = "migrate")
    @Autowired
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = new Flyway();

        flyway.setBaselineOnMigrate(true);
        flyway.setLocations("classpath:database/");
        flyway.setDataSource(dataSource);

        return flyway;
    }

    @Bean
    @Autowired
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    @Autowired
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }
}