package ru.kolomiec.taskspring.schedullers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kolomiec.taskspring.dto.TaskDTO;
import ru.kolomiec.taskspring.entity.Task;
import ru.kolomiec.taskspring.facade.TaskFacade;
import ru.kolomiec.taskspring.repository.TaskRepository;
import ru.kolomiec.taskspring.services.TaskServiceImpl;
import ru.kolomiec.taskspring.util.TimeUtil;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskRemindScheduler {


    private final TaskServiceImpl taskService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Async
    @Scheduled(timeUnit = TimeUnit.SECONDS, fixedRate = 20)
    public void isTimeToDoTask() {
        List<Task> list = taskService.getAllTasksWhichToDoTimeIsCurrentTime();
        if (!list.isEmpty()) {
            list.stream().forEach(a -> {
                log.info(String.format("writing in kafka task, owner is %s, which time is current", a.getOwner().toString()));
                kafkaTemplate.send("task", a.getOwner().getUsername(), a.getTaskName());
                taskService.deleteTaskById(a.getId());
            });
        }
    }
}
