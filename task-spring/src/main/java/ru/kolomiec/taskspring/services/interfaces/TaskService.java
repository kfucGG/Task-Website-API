package ru.kolomiec.taskspring.services.interfaces;

import ru.kolomiec.taskspring.entity.Task;

import java.util.List;

public interface TaskService {

    List<Task> getAllTaskByUserId(Long id);
}
