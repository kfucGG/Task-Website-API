package ru.kolomiec.bot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.kolomiec.bot.MainBot;
import ru.kolomiec.dto.TaskDTO;
import ru.kolomiec.requests.TaskApiRequest;

import java.util.Arrays;

public class NewTaskCommand extends AbstractCommand {
    public NewTaskCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendMessageIfUserCannotUseCommand(absSender, chat.getId());
        taskService.saveNewTaskToApi(chat.getId(), strings);
        sendMessage(absSender, "new task is saved",chat.getId().toString());
    }
}
