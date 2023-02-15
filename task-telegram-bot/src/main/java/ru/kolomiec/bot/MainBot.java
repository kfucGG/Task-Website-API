package ru.kolomiec.bot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.kolomiec.bot.commands.*;
import ru.kolomiec.database.dao.PersonDAO;
import ru.kolomiec.dto.TaskDTO;
import ru.kolomiec.service.TaskService;
import ru.kolomiec.util.ReplyKeyboardUtil;

import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MainBot extends TelegramLongPollingCommandBot {

    private final String KEY = "6116678801:AAEzHaMCHlMbyF56KSTvL5PW8oiIYAdmN78";
    private final String BOT_NAME = "Task385Bot";
    private final PersonDAO personDAO = new PersonDAO();
    private final TaskService taskService = new TaskService();
    private TaskCreateSession taskCreateSession = null;
    private boolean isNextMessageIsTask = false;
    public MainBot() {
        register(new StartCommand("/start", "start command"));
        register(new HelpCommand("/help", "shows all commands"));
        register(new RegistrationCommand("/registration", "registration on api"));
    }
    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    public String getBotToken() {
        return KEY;
    }



    @Override
    @SneakyThrows
    public void processNonCommandUpdate(Update update)  {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        if (!personDAO.isPersonSavedInDb(chatId)) {
            execute(new SendMessage().builder().chatId(chatId).text("Вы не аутентифицированы на API").build());
            return;
        }

        if (update.getMessage().getText().contains("all tasks")) {
            TaskDTO[] tasks = taskService.getAllTasksFromApi(chatId);
            sendKeyboard(update,"Все ваши задачи: \n%s".formatted(taskService.arrayDTOToString(tasks)), ReplyKeyboardUtil.getMainKeyboard());
            taskCreateSession = null;
            return;
        }

        if (update.getMessage().getText().contains("new task")) {
            taskCreateSession = new TaskCreateSession();
            isNextMessageIsTask = true;
            execute(new SendMessage().builder().chatId(chatId).text("Введите название задачи").build());
            return;
        }

        if (isNextMessageIsTask) {
            taskCreateSession.setTaskDTO(new TaskDTO(message.getText(), null));
            isNextMessageIsTask = false;
            sendKeyboard(update, "Когда задача должна быть выполнена(вам придет напоминание)", ReplyKeyboardUtil.getChooseTaskTimeKeyboard());
            return;
        }
        if (taskCreateSession != null) {

            if (message.getText().contains("another day")) {
                taskCreateSession.setTimeToDo(TimeToDo.ANOTHER_DAY);
                execute(SendMessage.builder().chatId(chatId).text("Введите дату и время в след формате yyyy-MM-ddTHH:mm:ss").build());
                return;
            }

            if (message.getText().contains("today")) {
                taskCreateSession.setTimeToDo(TimeToDo.TODAY);
                execute(SendMessage
                        .builder()
                        .chatId(chatId)
                        .text("Введите время в следующем формате: HH:mm or HH:mm:ss").build());
                return;
            }

            if (message.getText().contains("no time")) {
                taskService.saveNewTaskToApi(chatId, taskCreateSession.getTaskDTO());
                sendKeyboard(update, "Задача сохранена без времени!", ReplyKeyboardUtil.getMainKeyboard());
                taskCreateSession = null;
                return;
            }

            if (taskCreateSession.getTimeToDo().compareTo(TimeToDo.ANOTHER_DAY) == 0) {
                LocalDateTime taskTime = LocalDateTime.parse(message.getText(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                taskCreateSession.setTaskDTO(new TaskDTO(taskCreateSession.getTaskDTO().getTaskName(), taskTime));
                taskService.saveNewTaskToApi(chatId, taskCreateSession.getTaskDTO());
                taskCreateSession = null;
                sendKeyboard(update, "Задача сохранена!", ReplyKeyboardUtil.getMainKeyboard());
                return;
            }

            if (taskCreateSession.getTimeToDo().compareTo(TimeToDo.TODAY) == 0) {
                taskCreateSession.setTaskDTO(new TaskDTO(taskCreateSession.getTaskDTO().getTaskName(),
                        LocalTime.parse(message.getText()).atDate(LocalDate.now())));
                taskService.saveNewTaskToApi(chatId, taskCreateSession.getTaskDTO());
                taskCreateSession = null;
                sendKeyboard(update, "Задача сохранена!", ReplyKeyboardUtil.getMainKeyboard());
                return;
            }
        }
        sendKeyboard(update, "Выберите функцию", ReplyKeyboardUtil.getMainKeyboard());
    }

    @SneakyThrows
    private void sendKeyboard(Update update, String text, ReplyKeyboard keyBoard) {
        execute(new SendMessage().builder().text(text).chatId(update
                            .getMessage()
                            .getChatId())
                    .replyMarkup(keyBoard).build());
    }
}
