package ru.kolomiec.taskspring.exceptions.customexceptions;

public class EmptyPersonTasksException extends RuntimeException {

    public EmptyPersonTasksException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
