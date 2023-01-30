package ru.kolomiec.taskspring.services.interfaces;

import ru.kolomiec.taskspring.dto.TaskDTO;
import ru.kolomiec.taskspring.entity.PersonDetailsSecurityEntity;
import ru.kolomiec.taskspring.entity.Task;

import java.util.List;

public interface TaskService {

    List<Task> getAllTaskByUserId(Long id);

    List<Task> getAllTaskByPersonUsername(String username);

    void saveTaskToPerson(PersonDetailsSecurityEntity authenticatedPerson, TaskDTO taskDTO);

    void deleteTaskOwnedByPerson(PersonDetailsSecurityEntity authenticatedPerson, Long taskId);
}
