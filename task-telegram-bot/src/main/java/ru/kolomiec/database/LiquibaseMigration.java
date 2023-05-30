package ru.kolomiec.database;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.SneakyThrows;
import ru.kolomiec.database.entity.enums.DatabaseProperties;
import ru.kolomiec.util.DatabasePropertiesReaderUtil;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;


public class LiquibaseMigration {


    @SneakyThrows
    public void doMigration() {
        Properties databaseProperties = DatabasePropertiesReaderUtil.getDatabaseConnectionProperties();
        Connection connection = DriverManager.getConnection(
                databaseProperties.getProperty(DatabaseProperties.URL.toString()),
                databaseProperties.getProperty(DatabaseProperties.USERNAME.toString()),
                databaseProperties.getProperty(DatabaseProperties.PASSWORD.toString()));
        Database database1 = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        Liquibase liquibase = new liquibase.Liquibase
                ("/db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database1);
        liquibase.update(new Contexts(), new LabelExpression());
    }

}
