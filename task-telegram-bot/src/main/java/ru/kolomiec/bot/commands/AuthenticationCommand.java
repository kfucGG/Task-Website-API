package ru.kolomiec.bot.commands;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class AuthenticationCommand extends AbstractCommand{

    public AuthenticationCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        authService.login(buildPersonFromArrayOfStringAndChatId(strings, chat));
        sendMessage(absSender, "your token is saved in db, you are authenticated", chat.getId().toString());
    }
}
