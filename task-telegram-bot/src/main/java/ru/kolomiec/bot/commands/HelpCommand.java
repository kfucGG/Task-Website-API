package ru.kolomiec.bot.commands;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.commands.GetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class HelpCommand extends AbstractCommand {

    public HelpCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        StringBuilder allCommandsDescription = new StringBuilder();
        allCommandsDescription.append("/help ").append("return all commands with some description").append("\n");
        allCommandsDescription.append("/registration ").append("need to use next pattern /registration [username] [password] for " +
                "registration on api").append("\n");
        allCommandsDescription.append("/authorization ").append("if you registered on api earlier, you should use that command with username and password").append("\n");
        allCommandsDescription.append("/mytasks ").append("show all your tasks").append("\n");
        allCommandsDescription.append("/newtask [task] [(optional) time , format {yyyy-MM-ddThh:mm:ss}]").append("adding task").append("\n");
        sendMessage(absSender, allCommandsDescription.toString(), chat.getId().toString());
    }
}
