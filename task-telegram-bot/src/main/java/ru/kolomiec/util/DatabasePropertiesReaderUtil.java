package ru.kolomiec.util;

import lombok.SneakyThrows;

import java.io.FileInputStream;
import java.util.Properties;

public class DatabasePropertiesReaderUtil {


    @SneakyThrows
    public static Properties getDatabaseConnectionProperties() {
        Properties properties = new Properties(
        );
        properties.load(new FileInputStream("task-telegram-bot/src/main/resources/database.properties"));
        return properties;
    }
}
