package ru.kolomiec.util;

import org.checkerframework.checker.units.qual.K;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public class ReplyKeyboardUtil {

    public static ReplyKeyboardMarkup getMainKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("\uD83D\uDCDDnew task");
        row1.add("\uD83D\uDCCBall tasks");
        markup.setKeyboard(List.of(row1));
        return markup;
    }

    public static ReplyKeyboardMarkup getChooseTaskTimeKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("\uD83D\uDDD3no time");
        row1.add("\uD83D\uDCC6today");
        row1.add("\uD83D\uDCC5another day");
        markup.setKeyboard(List.of(row1));
        return markup;
    }
}
