package ru.kolomiec.taskspring.exceptions.customexceptions;

public class PersonWithSuchUsernameAlreadyExists extends RuntimeException {

    public PersonWithSuchUsernameAlreadyExists() {
        super("person with such username already exists");
    }

    public PersonWithSuchUsernameAlreadyExists(String message) {
        super(message);
    }
}
