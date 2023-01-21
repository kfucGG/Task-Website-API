package ru.kolomiec.taskspring.services;

import org.springframework.data.jpa.repository.Query;
import ru.kolomiec.taskspring.entity.Task;

import java.util.List;

public interface TaskService {

    List<Task> getAllTaskByUserId(Long id);
}
