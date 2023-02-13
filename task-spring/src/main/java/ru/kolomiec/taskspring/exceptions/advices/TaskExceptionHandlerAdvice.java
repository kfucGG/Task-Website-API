package ru.kolomiec.taskspring.exceptions.advices;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.kolomiec.taskspring.aspects.LogExceptionHandlerAdvice;
import ru.kolomiec.taskspring.exceptions.ResponseException;
import ru.kolomiec.taskspring.exceptions.customexceptions.EmptyPersonTasksException;
import ru.kolomiec.taskspring.exceptions.customexceptions.PersonHaveNotSuchTaskException;

import java.util.Date;

@RestControllerAdvice
@LogExceptionHandlerAdvice
public class TaskExceptionHandlerAdvice {


    @ExceptionHandler(EmptyPersonTasksException.class)
    public ResponseEntity<ResponseException> personHaveNotTasks(EmptyPersonTasksException e) {
        return new ResponseEntity(new ResponseException(e.getMessage(), new Date()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PersonHaveNotSuchTaskException.class)
    public ResponseEntity<ResponseException> personHaveNotTask(PersonHaveNotSuchTaskException e) {
        return new ResponseEntity<>(new ResponseException(e.getMessage(), new Date()), HttpStatus.BAD_REQUEST);
    }
}
