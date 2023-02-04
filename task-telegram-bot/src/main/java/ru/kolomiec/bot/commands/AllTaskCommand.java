package ru.kolomiec.bot.commands;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.kolomiec.dto.TaskDTO;

import java.util.Arrays;

public class AllTaskCommand extends AbstractCommand{

    public AllTaskCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendMessageIfUserCannotUseCommand(absSender, chat.getId());
        String allTasks = convertTaskArrayToString(taskService.getAllTasksFromApi(chat.getId()));
        sendMessage(absSender, allTasks, chat.getId().toString());
    }
    private String convertTaskArrayToString(TaskDTO[] tasks) {
        StringBuilder tasksInListFormat = new StringBuilder();
        System.out.println(Arrays.toString(tasks));
        for (int i = 0; i < tasks.length; i++) {
            tasksInListFormat.append(i + 1).append(". ").append(tasks[i].getTaskName()).append(" time: ")
                    .append(tasks[i].getToDoTime()).append("\n");
        }
        return tasksInListFormat.toString();
    }
}
