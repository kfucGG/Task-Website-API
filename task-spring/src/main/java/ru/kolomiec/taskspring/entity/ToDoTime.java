package ru.kolomiec.taskspring.entity;


import jakarta.persistence.Embeddable;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ToDoTime {

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime taskTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate taskDate;
}
