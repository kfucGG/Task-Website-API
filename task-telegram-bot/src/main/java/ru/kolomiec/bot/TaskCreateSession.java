package ru.kolomiec.bot;

import lombok.Getter;
import lombok.Setter;
import ru.kolomiec.dto.TaskDTO;


@Getter
@Setter
public class TaskCreateSession {
    private TimeToDo timeToDo;
    private TaskDTO taskDTO;
}

enum TimeToDo {
    TODAY, NOTIME, ANOTHER_DAY
}