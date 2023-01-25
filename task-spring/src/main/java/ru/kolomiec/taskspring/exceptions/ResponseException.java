package ru.kolomiec.taskspring.exceptions;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class ResponseException {

    private String message;

    @JsonFormat(pattern = "dd-mm-yyyy hh:mm:ss")
    private LocalDateTime time;
}
