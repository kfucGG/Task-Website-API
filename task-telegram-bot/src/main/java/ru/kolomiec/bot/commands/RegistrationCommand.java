package ru.kolomiec.bot.commands;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;


public class RegistrationCommand extends AbstractCommand {

    public RegistrationCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    @SneakyThrows
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        authService.registration(buildPersonFromArrayOfStringAndChatId(strings, chat));
        absSender.execute(new SendMessage().builder().text("your token is saved in db, you are registered").chatId(chat.getId()).replyMarkup(getKeyboard()).build());
    }
    private ReplyKeyboardMarkup getKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardButton button = new KeyboardButton();
        row1.add("new task");
        row1.add("all tasks");
        markup.setKeyboard(List.of(row1));
        return markup;
    }
}
