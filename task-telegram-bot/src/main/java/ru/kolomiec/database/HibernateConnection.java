package ru.kolomiec.database;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import ru.kolomiec.database.entity.AuthToken;
import ru.kolomiec.database.entity.Person;
import ru.kolomiec.database.entity.enums.DatabaseProperties;
import ru.kolomiec.util.DatabasePropertiesReaderUtil;

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
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Person.class);
        configuration.addAnnotatedClass(AuthToken.class);
        configuration.addProperties(getHibernateDatasource());
        configuration.addProperties(getHibernateSettings());
        return configuration;
    }
    private static Properties getHibernateDatasource() {
        Properties dbProperties = DatabasePropertiesReaderUtil.getDatabaseConnectionProperties();
        if (System.getenv("FROM_DOCKER") != null) {
            return getHibernateDockerDatasource();
        }
        Properties hibernateDatasourceProperties = new Properties();
        hibernateDatasourceProperties.put(Environment.URL,
                dbProperties.get(DatabaseProperties.URL.toString()));
        hibernateDatasourceProperties.put(Environment.USER,
                dbProperties.get(DatabaseProperties.USERNAME.toString()));
        hibernateDatasourceProperties.put(Environment.PASS,
                dbProperties.get(DatabaseProperties.PASSWORD.toString()));

        return hibernateDatasourceProperties;
    }
    private static Properties getHibernateDockerDatasource() {
        Properties properties = new Properties();
        properties.setProperty(Environment.URL, System.getenv("POSTGRES_URL"));
        properties.setProperty(Environment.USER, System.getenv("POSTGRES_USERNAME"));
        properties.setProperty(Environment.PASS, System.getenv("POSTGRES_PASSWORD"));
        return properties;
    }
    private static Properties getHibernateSettings() {
        Properties properties = new Properties();
        properties.setProperty(Environment.DRIVER, "org.postgresql.Driver");
        properties.setProperty(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        properties.setProperty(Environment.SHOW_SQL, "false");
        properties.setProperty(Environment.HBM2DDL_AUTO, "validate");
        return properties;
    }
}
