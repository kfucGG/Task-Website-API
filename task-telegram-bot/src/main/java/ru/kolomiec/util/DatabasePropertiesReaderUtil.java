package ru.kolomiec.util;

import lombok.SneakyThrows;

import javax.swing.plaf.basic.BasicProgressBarUI;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class DatabasePropertiesReaderUtil {


    @SneakyThrows
    public static Properties getDatabaseConnectionProperties() {
        Properties properties = new Properties();
        InputStream resourceAsStream = DatabasePropertiesReaderUtil.class
                .getClassLoader().getResourceAsStream("database.properties");
        properties.load(resourceAsStream);
        return properties;
    }
}
