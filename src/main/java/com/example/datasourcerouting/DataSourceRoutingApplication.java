package com.example.datasourcerouting;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@SpringBootApplication
@EnableConfigurationProperties(DatabaseConfigurations.class)
public class DataSourceRoutingApplication {
    private static final Logger log = LoggerFactory.getLogger(DataSourceRoutingApplication.class);
    private DatabaseConfigurations databaseConfigurations;

    @Autowired
    public DataSourceRoutingApplication(DatabaseConfigurations databaseConfigurations) {
        this.databaseConfigurations = databaseConfigurations;
    }

    @Bean
    public DataSource dataSource() {
        HotelRoutingDataSource routingDataSource = new HotelRoutingDataSource();
        routingDataSource.setTargetDataSources(this.databaseConfigurations.createTargetDataSources());
        return  routingDataSource;
    }

//    @Bean
//    public LocalSessionFactoryBean sessionFactory() {
//        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
//        sessionFactoryBean.setDataSource(dataSource());
//        Properties properties = new Properties();
//        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
//        sessionFactoryBean.setHibernateProperties(properties);
//        return sessionFactoryBean;
//    }
//
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(dataSource());
//        em.setPackagesToScan("com.example.datasourcerouting");
//        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        em.setJpaVendorAdapter(vendorAdapter);
//        Properties properties = new Properties();
//        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
//        em.setJpaProperties(properties);
//        return em;
//    }
//
//    @Bean
//    public PlatformTransactionManager transactionManager() {
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
//        return transactionManager;
//    }

    @Bean
    public CommandLineRunner demo(HotelRepository repository, JdbcTemplate jdbcTemplate) {
        return (args) -> {
            HotelContextHolder.setDatabase("h2-pb");
            jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS HOTEL(ID INT PRIMARY KEY, CODE VARCHAR(255) DEFAULT '',NAME VARCHAR(255) DEFAULT '');");
            // save a few customers
            repository.save(new Hotel(1, "1qwvv", "Majestic"));
            repository.save(new Hotel(2, "vcvc", "Ibis"));
            HotelContextHolder.setDatabase("h2-pa");
            jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS HOTEL(ID INT PRIMARY KEY, CODE VARCHAR(255) DEFAULT '',NAME VARCHAR(255) DEFAULT '');");
            repository.save(new Hotel(3, "zxcv", "Mercure"));
            repository.save(new Hotel(4, "33df", "Novotel"));

            // fetch all hotels
            log.info("Hotels found with findAll():");
            for (Hotel customer : repository.findAll()) {
                log.info(customer.toString());
            }
            log.info("");

            // fetch an individual hotel by ID
            Hotel customer = repository.findById(1);
            log.info("Hotel found with findById(1):");
            log.info(customer.toString());
            log.info("");

            // fetch customers by last name
            log.info("Hotel found with name ('Ibis'):");
            repository.findByName("Ibis").forEach(bauer -> {
                log.info(bauer.toString());
            });
            log.info("");
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(DataSourceRoutingApplication.class, args);
    }
}
