package ru.kolomiec.bot.commands;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.kolomiec.database.dao.PersonDAO;

public class StartCommand extends AbstractCommand {

    public StartCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        if (!personDAO.isPersonSavedInDb(chat.getId())) {
            sendMessage(absSender, "you are auth", chat.getId().toString());
        } else {
            sendMessage(absSender, "you need to call /registration [username] [password] or /auth", chat.getId().toString());
        }
    }
}
