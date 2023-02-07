package ru.kolomiec.taskspring.schedullers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kolomiec.taskspring.entity.Task;
import ru.kolomiec.taskspring.repository.TaskRepository;
import ru.kolomiec.taskspring.services.TaskServiceImpl;
import ru.kolomiec.taskspring.util.TimeUtil;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Component
public class TaskRemindScheduler {

    @Autowired
    TaskServiceImpl taskService;

    @Async
    @Scheduled(timeUnit = TimeUnit.SECONDS, fixedRate = 10)
    public void isTimeToDoTask() {
        var list = taskService.getAllTasksWhichToDoTimeIsCurrentTime();
        if (!list.isEmpty()) {
            list.stream().forEach(a -> {
                System.out.println(a.getTaskName() + a.getOwner().getUsername());
            });
        }
    }
}
