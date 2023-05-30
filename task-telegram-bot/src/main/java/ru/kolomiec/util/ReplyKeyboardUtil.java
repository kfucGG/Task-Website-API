package ru.kolomiec.util;

import org.checkerframework.checker.units.qual.K;
import org.telegram.telegrambots.meta.api.objects.games.CallbackGame;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.kolomiec.dto.TaskDTO;

import java.util.ArrayList;
import java.util.List;

public class ReplyKeyboardUtil {

    public static ReplyKeyboardMarkup getMainKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        row1.add("\uD83D\uDCDDnew task");
        row1.add("\uD83D\uDCCBall tasks");
        row2.add("\uD83D\uDDD1delete task");
        markup.setKeyboard(List.of(row1, row2));
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

    public static InlineKeyboardMarkup getInlineKeyboardForDeletingTask(
            TaskDTO[] tasks
    ) {
        InlineKeyboardMarkup taskDeletingKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        for (TaskDTO task : tasks) {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            if (task.getToDoTime() != null) {
                inlineKeyboardButton.setText(String.format(
                        "%s время: %s", task.getTaskName(), task.getToDoTime().getTaskTime()
                ));
            } else {
                inlineKeyboardButton.setText(task.getTaskName());
            }
            int id = task.getId().intValue();
            inlineKeyboardButton.setCallbackData(StringUtil.longTaskIdToPermittedStringForCallbackData(task.getId()));
            lists.add(List.of(inlineKeyboardButton));
        }
        taskDeletingKeyboard.setKeyboard(lists);
        return taskDeletingKeyboard;
    }
}
