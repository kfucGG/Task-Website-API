package ru.kolomiec.taskspring.exceptions;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class ResponseException {

    private String message;

    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    private Date time;
}
