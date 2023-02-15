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

import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


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
        register(new AuthenticationCommand("/authentication", "authentication on api"));
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
        System.out.println("in non proccess method");
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        if (!personDAO.isPersonSavedInDb(chatId)) {
            System.out.println("person does not exists");
            execute(new SendMessage().builder().chatId(chatId).text("you are not auth").build());
            return;
        }

        if (update.getMessage().getText().equals("all tasks")) {
            System.out.println("person is want to get all tasks");
            TaskDTO[] tasks = taskService.getAllTasksFromApi(chatId);
            taskCreateSession = null;
            sendKeyboard(update,"all your task: %s".formatted(Arrays.toString(tasks)), getMainKeyboard());
            return;
        }

        if (update.getMessage().getText().equals("new task")) {
            System.out.println("person is decided to create new task");
            taskCreateSession = new TaskCreateSession();
            isNextMessageIsTask = true;
            try {
                execute(new SendMessage().builder().chatId(chatId).text("enter task name").build());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        if (isNextMessageIsTask) {
            System.out.println("user input a task name");
            String text = update.getMessage().getText();
            taskCreateSession.setTaskDTO(new TaskDTO(text, null));
            isNextMessageIsTask = false;
            sendKeyboard(update, "When task should be done?", getChooseTaskTimeKeyboard());
            return;
        }

        if (message.getText().equals("another day")) {
            System.out.println("message is another day");
            taskCreateSession.setTimeToDo(TimeToDo.ANOTHER_DAY);
            execute(SendMessage.builder().chatId(chatId).text("input date in format yyyy-MM-ddTHH:mm:ss").build());
            return;
        }

        if (message.getText().equals("today")) {
            System.out.println("message is today");
            taskCreateSession.setTimeToDo(TimeToDo.TODAY);
            execute(SendMessage
                    .builder()
                    .chatId(chatId)
                    .text("Input time in format HH:mm or HH:mm:ss").build());
            return;
        }

        if (message.getText().equals("no time")) {
            System.out.println("message is no time");
            taskService.saveNewTaskToApi(chatId, taskCreateSession.getTaskDTO());
            sendKeyboard(update, "task is saved without time", getMainKeyboard());
            taskCreateSession = null;
            return;
        }

        if (taskCreateSession.getTimeToDo().compareTo(TimeToDo.ANOTHER_DAY) == 0) {
            System.out.println("another day");
            LocalDateTime taskTime = LocalDateTime.parse(message.getText(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            taskCreateSession.setTaskDTO(new TaskDTO(taskCreateSession.getTaskDTO().getTaskName(), taskTime));
            taskService.saveNewTaskToApi(chatId, taskCreateSession.getTaskDTO());
            taskCreateSession = null;
            sendKeyboard(update, "Task is saved", getMainKeyboard());
            return;
        }

        if (taskCreateSession.getTimeToDo().compareTo(TimeToDo.TODAY) == 0) {
            System.out.println("today");
            System.out.println("message is" + message.getText());
            var time = LocalTime.parse(message.getText());
            System.out.println("time is" + time);
            taskCreateSession.setTaskDTO(new TaskDTO(taskCreateSession.getTaskDTO().getTaskName(), time.atDate(LocalDate.now())));
            taskService.saveNewTaskToApi(chatId, taskCreateSession.getTaskDTO());
            taskCreateSession = null;
            sendKeyboard(update, "Task is saved", getMainKeyboard());
            return;
        }
        sendKeyboard(update, "Choose option", getMainKeyboard());
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
