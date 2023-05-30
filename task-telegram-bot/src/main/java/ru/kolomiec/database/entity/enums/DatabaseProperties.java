package ru.kolomiec.database.entity.enums;

public enum DatabaseProperties {

    URL("db_url"), USERNAME("db_username"), PASSWORD("db_password");

    private final String propsKey;

    private DatabaseProperties (String propertyKey) {
        this.propsKey = propertyKey;
    }

    @Override
    public String toString() {
        return propsKey;
    }
}
