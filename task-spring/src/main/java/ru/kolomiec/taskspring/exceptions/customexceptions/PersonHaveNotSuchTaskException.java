package ru.kolomiec.taskspring.exceptions.customexceptions;

public class PersonHaveNotSuchTaskException extends RuntimeException {

    public PersonHaveNotSuchTaskException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
