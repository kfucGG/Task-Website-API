package ru.kolomiec.bot.commands;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.kolomiec.requests.TaskApiRequest;

public class NewTaskCommand extends AbstractCommand {
    public NewTaskCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendMessage(absSender, "ok", chat.getId().toString());
        TaskApiRequest request = new TaskApiRequest();
        request.saveNewTaskToApi(chat.getId());
    }
}
