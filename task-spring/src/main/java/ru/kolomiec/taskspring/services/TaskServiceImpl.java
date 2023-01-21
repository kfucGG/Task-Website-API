package ru.kolomiec.taskspring.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolomiec.taskspring.entity.Task;
import ru.kolomiec.taskspring.repository.TaskRepository;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> getAllTaskByUserId(Long id) {
        return taskRepository.findAllByOwnerId(id).orElseThrow(() -> new NullPointerException());//TODO custom exception
    }
}
