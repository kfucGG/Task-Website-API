package ru.kolomiec.bot.commands;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.kolomiec.database.dao.PersonDAO;
import ru.kolomiec.database.entity.Person;
import ru.kolomiec.service.AuthService;
import ru.kolomiec.service.TaskService;

abstract public class AbstractCommand extends BotCommand {

    protected PersonDAO personDAO = new PersonDAO();
    public AuthService authService = new AuthService();
    public TaskService taskService = new TaskService();
    public AbstractCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    public void sendMessageIfUserCannotUseCommand(AbsSender sender, Long chatId) {
        if (!personDAO.isPersonSavedInDb(chatId)) {
            sendMessage(sender, "you can not use this command, /registration on /authorization on api"
                    , chatId.toString());
        }
    }

    public void sendMessage(AbsSender sender, String text, String chatId) {
        trySendMessage(sender, getSendMessage(text, chatId));
    }
    public Person buildPersonFromArrayOfStringAndChatId(String[] strings, Chat chat) {
        Person person = new Person();
        person.setUsername(strings[0]);
        person.setPassword(strings[1]);
        person.setChatId(chat.getId());
        return person;
    }
    private SendMessage getSendMessage(String message, String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(message);
        sendMessage.setChatId(chatId);
        return sendMessage;
    }
    private void trySendMessage(AbsSender sender, SendMessage sendMessage) {
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException ex) {
            ex.printStackTrace();
        }
    }
}
