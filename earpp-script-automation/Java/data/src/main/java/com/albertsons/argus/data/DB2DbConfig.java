package com.albertsons.argus.data;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "db2EntityManagerFactory",
    transactionManagerRef = "db2TransactionManager", basePackages = {"com.albertsons.argus.data.repo.db2"})
public class DB2DbConfig {
  
  @Autowired
  private Environment environment;

  @Bean(name = "db2DataSource")
  @ConfigurationProperties(prefix = "db2.datasource")
  public DataSource dataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean(name = "db2EntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean db2EntityManagerFactory(
      EntityManagerFactoryBuilder builder, @Qualifier("db2DataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = builder.dataSource(dataSource).packages("com.albertsons.argus.data.bo.common", "com.albertsons.argus.data.bo.db2").persistenceUnit("db2")
        .build();

       em.setJpaProperties(additionalProperties());
        
    return em;
  }

  Properties additionalProperties() {
    Properties properties = new Properties();

    properties.setProperty("hibernate.dialect", environment.getProperty("db2.hibernate.dialect"));
    return properties;
}

  @Bean(name = "db2TransactionManager")
  public PlatformTransactionManager db2TransactionManager(
      @Qualifier("db2EntityManagerFactory") EntityManagerFactory db2EntityManagerFactory) {
    return new JpaTransactionManager(db2EntityManagerFactory);
  }

}
