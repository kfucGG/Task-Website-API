package ru.kolomiec.taskspring.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolomiec.taskspring.aspects.ServiceLog;
import ru.kolomiec.taskspring.dto.TaskDTO;
import ru.kolomiec.taskspring.entity.PersonDetailsSecurityEntity;
import ru.kolomiec.taskspring.entity.Task;
import ru.kolomiec.taskspring.exceptions.customexceptions.EmptyPersonTasksException;
import ru.kolomiec.taskspring.exceptions.customexceptions.PersonHaveNotSuchTaskException;
import ru.kolomiec.taskspring.facade.TaskFacade;
import ru.kolomiec.taskspring.repository.TaskRepository;
import ru.kolomiec.taskspring.services.interfaces.PersonService;
import ru.kolomiec.taskspring.services.interfaces.TaskService;
import ru.kolomiec.taskspring.util.TimeUtil;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@ServiceLog
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final PersonService personService;
    private final TaskFacade taskFacade;
    @Override
    public List<Task> getAllTaskByUserId(Long id) {
        return taskRepository.findAllByOwnerId(id).orElseThrow(() -> new EmptyPersonTasksException("you have not tasks"));
    }

    @Override
    public List<Task> getAllTaskByPersonUsername(String username) {
        List<Task> personTasks = taskRepository.findAllByOwnerUsername(username).get();
        checkListTaskIsEmpty(personTasks);
        return personTasks;
    }

    @Override
    @Transactional
    public void saveTaskToPerson(PersonDetailsSecurityEntity authenticatedPerson, TaskDTO taskDTO) {
        Task newTaskToPerson = taskFacade.fromTaskDTOToTask(taskDTO);
        newTaskToPerson.setOwner(personService.findByUsername(authenticatedPerson.getUsername()));
        taskRepository.save(newTaskToPerson);
    }

    @Override
    @Transactional
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteTaskOwnedByPerson(PersonDetailsSecurityEntity authenticatedPerson, Long taskId) {
        checkTaskIsOwnerByPerson(authenticatedPerson, taskId);
        taskRepository.deleteById(taskId);
    }

    public List<Task> getAllTasksWhichToDoTimeIsCurrentTime() {
        return taskRepository.finaAllTasksWhichToDoTimeIsCurrentDateAndTime(
                LocalTime.parse(TimeUtil.getCurrentTimeInHoursAndMinutesFormat())
        ).orElse(Collections.emptyList());
    }

    private void checkTaskIsOwnerByPerson(PersonDetailsSecurityEntity authenticatedPerson, Long taskId) {
        taskRepository.findTaskByOwnerUsernameAndTaskId(authenticatedPerson.getUsername(), taskId).orElseThrow(() -> {
            return new PersonHaveNotSuchTaskException("you have not such task");
        });
    }

    private void checkListTaskIsEmpty(List<Task> personTasks) {
        if (personTasks.isEmpty()) throw new EmptyPersonTasksException("you have not tasks");
    }
}
