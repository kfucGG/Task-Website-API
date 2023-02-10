package ru.kolomiec.database;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.kolomiec.database.entity.AuthToken;
import ru.kolomiec.database.entity.Person;

import java.util.Properties;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HibernateConnection {


    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            return sessionFactory = getHibernateConfiguration().buildSessionFactory();
        }
        return sessionFactory;
    }
    private static Configuration getHibernateConfiguration() {
        System.getenv().entrySet().stream().forEach(a -> System.out.println(a.getKey() +  " " + a.getValue()));
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Person.class);
        configuration.addAnnotatedClass(AuthToken.class);
        configuration.addProperties(getHibernateDatasource());
        configuration.addProperties(getHibernateSettings());
        return configuration;
    }
    private static Properties getHibernateSettings() {
        Properties properties = new Properties();
        if (!System.getenv("POSTGRES_URL").isBlank()) {
            System.out.println("im using docker settings");
            return getHibernateDockerDatasource();
        }
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty("hibernate.current_session_context_class", "thread");
        properties.setProperty("hibernate.show_sql", "true");
        return properties;
    }
    private static Properties getHibernateDockerDatasource() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        properties.setProperty("hibernate.connection.url", System.getenv("POSTGRES_URL"));
        properties.setProperty("hibernate.connection.username", System.getenv("POSTGRES_USERNAME"));
        properties.setProperty("hibernate.connection.password", System.getenv("POSTGRES_PASSWORD"));
        return properties;
    }
    private static Properties getHibernateDatasource() {
        Properties properties  = new Properties();
        properties.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        properties.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/task_telegram");
        properties.setProperty("hibernate.connection.username", "root");
        properties.setProperty("hibernate.connection.password", "root");
        return properties;
    }
}
