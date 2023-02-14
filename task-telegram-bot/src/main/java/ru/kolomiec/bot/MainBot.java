package ru.kolomiec.bot;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;


public class MainBot extends TelegramLongPollingCommandBot {

    private final String key = "6116678801:AAEzHaMCHlMbyF56KSTvL5PW8oiIYAdmN78";
    private final PersonDAO personDAO = new PersonDAO();

    private final TaskService taskService = new TaskService();
    public MainBot() {
        register(new StartCommand("/start", "start command"));
        register(new HelpCommand("/help", "shows all commands"));
        register(new RegistrationCommand("/registration", "registration on api"));
        register(new AuthenticationCommand("/authentication", "authentication on api"));
    }
    @Override
    public String getBotUsername() {
        return "Task385Bot";
    }

    public String getBotToken() {
        return key;
    }

    private boolean isNextMessageIsTask = false;

    private boolean today = false;

    private boolean anotherDay = false;

    private boolean noTime = false;

    private TaskDTO task = null;

    Logger log = Logger.getLogger(this.getClass().getName());
    @Override
    public void processNonCommandUpdate(Update update)  {
        System.out.println("in non proccess method");
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        if (!personDAO.isPersonSavedInDb(chatId)) {
            System.out.println("person does not exists");
            try {
                execute(new SendMessage().builder().chatId(chatId).text("you are not auth").build());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        if (update.getMessage().getText().equals("all tasks")) {
            System.out.println("person is want to get all tasks");
            TaskDTO[] tasks = taskService.getAllTasksFromApi(chatId);
            task = null;
            sendKeyboard(update,"all your task: %s".formatted(Arrays.toString(tasks)), getMainKeyboard());
            return;
        }

        if (update.getMessage().getText().equals("new task")) {
            System.out.println("person is decided to create new task");
            task = new TaskDTO();
            isNextMessageIsTask = true;
            try {
                execute(new SendMessage().builder().chatId(chatId).text("enter task name").build());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        if (message.getText().equals("another day")) {
            System.out.println("message is another day");
            anotherDay = true;
            try {
                execute(SendMessage
                        .builder()
                        .chatId(chatId)
                        .text("input date in format yyyy-MM-ddTHH:mm:ss").build());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        if (message.getText().equals("today")) {
            System.out.println("message is today");
            today = true;
            try {
                execute(SendMessage
                        .builder()
                        .chatId(chatId)
                        .text("Input time in format HH:mm or HH:mm:ss").build());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        if (isNextMessageIsTask) {
            System.out.println("user input a task name");
            String text = update.getMessage().getText();
            task.setTaskName(text);
            isNextMessageIsTask = false;
            sendKeyboard(update, "When task should be done?", getChooseTaskTimeKeyboard());
            return;
        }

        if (message.getText().equals("no time")) {
            System.out.println("message is no time");
            taskService.saveNewTaskToApi(chatId, task);
            sendKeyboard(update, "task is saved without time", getMainKeyboard());
            task = null;
            return;
        }

        if (anotherDay) {
            System.out.println("another day");
            anotherDay = false;
            System.out.println(message.getText());
            task.setToDoTime(LocalDateTime.parse(message.getText(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            taskService.saveNewTaskToApi(chatId, task);
            task = null;
            sendKeyboard(update, "Task is saved", getMainKeyboard());
            return;
        }

        if (today) {
            System.out.println("today");
            var tt = LocalDateTime.parse(message.getText(), DateTimeFormatter.ISO_LOCAL_TIME);
            task.setToDoTime(tt);
            taskService.saveNewTaskToApi(chatId, task);
            task = null;
            return;
        }
        sendKeyboard(update, "Choose option", getMainKeyboard());
        log.info("end of method");
    }

    private void sendKeyboard(Update update, String text, ReplyKeyboard keyBoard) {
        try {
            execute(new SendMessage().builder().text(text).chatId(update
                            .getMessage()
                            .getChatId())
                    .replyMarkup(keyBoard).build());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    private ReplyKeyboardMarkup getMainKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("new task");
        row1.add("all tasks");
        markup.setKeyboard(List.of(row1));
        return markup;
    }

    private ReplyKeyboardMarkup getChooseTaskTimeKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("no time");
        row1.add("today");
        row1.add("another day");
        markup.setKeyboard(List.of(row1));
        return markup;
    }
}
