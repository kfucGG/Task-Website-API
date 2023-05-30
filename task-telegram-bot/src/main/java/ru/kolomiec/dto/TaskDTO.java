package ru.kolomiec.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDTO {

    private Long id;
    private String taskName;
    private ToDoTime toDoTime;

    public TaskDTO(String taskName, ToDoTime toDoTime) {
        this.taskName = taskName;
        this.toDoTime = toDoTime;
    }
}
