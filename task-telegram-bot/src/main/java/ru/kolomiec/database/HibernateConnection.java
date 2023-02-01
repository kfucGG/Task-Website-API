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

    private static HibernateConnection hibernateConnection;

    public static HibernateConnection getInstance() {
        if (hibernateConnection == null) hibernateConnection = new HibernateConnection();
        return hibernateConnection;
    }

    public SessionFactory getSessionFactory() {
        return getHibernateConfiguration().buildSessionFactory();
    }

    private Configuration getHibernateConfiguration() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Person.class);
        configuration.addAnnotatedClass(AuthToken.class);
        configuration.addProperties(getHibernateProperties());
        return configuration;
    }
    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        properties.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/task-telegram");
        properties.setProperty("hibernate.connection.username", "root");
        properties.setProperty("hibernate.connection.password", "root");
        properties.setProperty("hibernate.hbm2ddl.auto", "create");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty("hibernate.current_session_context_class", "thread");
        properties.setProperty("hibernate.show_sql", "true");
        return properties;
    }
}
