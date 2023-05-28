package ru.kolomiec.bot.commands;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.kolomiec.util.ReplyKeyboardUtil;

import java.util.List;


public class RegistrationCommand extends AbstractCommand {

    public RegistrationCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    @SneakyThrows
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        try {
            authService.registration(buildPersonFromArrayOfStringAndChatId(strings, chat));
        } catch (IllegalArgumentException e) {
            absSender.execute(new SendMessage().builder().text("Пользователь с таким именем уже существует, " +
                            "попробуйте заного")
                    .chatId(chat.getId()).build());
            return;
        }
        absSender.execute(new SendMessage().builder().text("Вы зарегистрированы!")
                .chatId(chat.getId()).replyMarkup(ReplyKeyboardUtil.getMainKeyboard()).build());
    }
}
