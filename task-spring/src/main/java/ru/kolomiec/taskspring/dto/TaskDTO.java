package ru.kolomiec.taskspring.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.kolomiec.taskspring.entity.Task;
import ru.kolomiec.taskspring.entity.ToDoTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaskDTO {

    @NotBlank(message = "can not be blank")
    @NotNull(message = "can not be null")
    @Size(min = 5, max = 100, message = "should be by 5 to 100 symbols")
    private String taskName;

    private ToDoTime toDoTime;

    public TaskDTO(Task task) {
        this.taskName = task.getTaskName();
        this.toDoTime = task.getToDoTime();
    }
    public Task toTask() {
        Task task = new Task();
        task.setTaskName(taskName);
        task.setToDoTime(toDoTime);
        return task;
    }
}
