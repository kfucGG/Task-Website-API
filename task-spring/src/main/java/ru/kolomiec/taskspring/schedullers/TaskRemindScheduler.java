package ru.kolomiec.taskspring.schedullers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.kolomiec.taskspring.repository.TaskRepository;
import ru.kolomiec.taskspring.util.TimeUtil;

import java.util.concurrent.TimeUnit;

@Component
public class TaskRemindScheduler {

    //select * from task where (to_do_time::time::text LIKE '14:00:%') AND (to_do_time::date::text LIKE '2022-03-03');
    @Autowired
    TaskRepository taskRepository;
    @Scheduled(timeUnit = TimeUnit.SECONDS, fixedRate = 5)
    @Async
    public void isTimeToDoTask() {
        System.out.println(TimeUtil.getCurrentDate());
        System.out.println(TimeUtil.getCurrentTimeInHoursAndMinutesFormat());
    }
}
