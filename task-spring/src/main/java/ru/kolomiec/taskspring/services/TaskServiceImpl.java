package ru.kolomiec.taskspring.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolomiec.taskspring.aspects.ServiceLog;
import ru.kolomiec.taskspring.dto.TaskDTO;
import ru.kolomiec.taskspring.entity.PersonDetailsSecurityEntity;
import ru.kolomiec.taskspring.entity.Task;
import ru.kolomiec.taskspring.entity.ToDoTime;
import ru.kolomiec.taskspring.exceptions.customexceptions.EmptyPersonTasksException;
import ru.kolomiec.taskspring.exceptions.customexceptions.PersonHaveNotSuchTaskException;
import ru.kolomiec.taskspring.repository.TaskRepository;
import ru.kolomiec.taskspring.services.interfaces.PersonService;
import ru.kolomiec.taskspring.services.interfaces.TaskService;
import ru.kolomiec.taskspring.util.TimeUtil;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@ServiceLog
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final PersonService personService;

    @Override
    public List<Task> getAllTaskByUserId(Long id) {
        log.info(String.format(
                "Person with id: %s try to retrieve all tasks", id
        ));
        return taskRepository.findAllByOwnerId(id).orElseThrow(() -> {
            log.info(String.format(
                    "Person with id: %s dont have tasks", id
            ));
            return new EmptyPersonTasksException("you have not tasks");
        });
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
        log.info(String.format(
                "Person: %s try to save task: %s", authenticatedPerson.getUsername(),taskDTO.toString()
        ));
        Task newTaskToPerson = taskDTO.toTask();
        newTaskToPerson.setOwner(authenticatedPerson.getPerson());
        taskRepository.save(newTaskToPerson);
    }

    @Override
    @Transactional
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteTaskOwnedByPerson(PersonDetailsSecurityEntity authenticatedPerson, TaskDTO taskDTO) {
        List<Task> tasksByTaskNameAndToDoTimeAndOwner = taskRepository.findTasksByTaskNameAndToDoTimeAndOwner(
                taskDTO.getTaskName(), taskDTO.getToDoTime(), authenticatedPerson.getPerson()
        );

        if (tasksByTaskNameAndToDoTimeAndOwner.isEmpty()) {
            throw new EmptyPersonTasksException("no such task found for deleting");
        }
        taskRepository.deleteAll(tasksByTaskNameAndToDoTimeAndOwner);
    }

    public List<Task> getAllTasksWhichToDoTimeIsCurrentTime() {
        return taskRepository.findAllTasksWhichToDoTimeIsCurrentDateAndTime(
                LocalTime.parse(TimeUtil.getCurrentTimeInHoursAndMinutesFormat())
        ).orElse(Collections.emptyList());
    }


    private void checkListTaskIsEmpty(List<Task> personTasks) {
        if (personTasks.isEmpty()) throw new EmptyPersonTasksException("you have not tasks");
    }
}
