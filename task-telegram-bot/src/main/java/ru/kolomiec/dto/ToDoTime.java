package ru.kolomiec.dto;


import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ToDoTime {

    private LocalTime taskTime;
    private LocalDate taskDate;
}
