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
import ru.kolomiec.dto.ToDoTime;
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
            execute(new SendMessage().builder().chatId(chatId).text("???? ???? ?????????????????????????????????? ???? API").build());
            return;
        }

        if (update.getMessage().getText().contains("all tasks")) {
            TaskDTO[] tasks = taskService.getAllTasksFromApi(chatId);
            sendKeyboard(update,"?????? ???????? ????????????: \n%s".formatted(taskService.arrayDTOToString(tasks)), ReplyKeyboardUtil.getMainKeyboard());
            taskCreateSession = null;
            return;
        }

        if (update.getMessage().getText().contains("new task")) {
            taskCreateSession = new TaskCreateSession();
            isNextMessageIsTask = true;
            execute(new SendMessage().builder().chatId(chatId).text("?????????????? ???????????????? ????????????").build());
            return;
        }

        if (isNextMessageIsTask) {
            taskCreateSession.setTaskDTO(new TaskDTO(message.getText(), null));
            isNextMessageIsTask = false;
            sendKeyboard(update, "?????????? ???????????? ???????????? ???????? ??????????????????(?????? ???????????? ??????????????????????)", ReplyKeyboardUtil.getChooseTaskTimeKeyboard());
            return;
        }
        if (taskCreateSession != null) {

            if (message.getText().contains("another day")) {
                taskCreateSession.setTimeToDo(TimeToDo.ANOTHER_DAY);
                execute(SendMessage.builder().chatId(chatId).text("?????????????? ???????? ?? ?????????? ?? ???????? ?????????????? yyyy-MM-dd HH:mm").build());
                return;
            }

            if (message.getText().contains("today")) {
                taskCreateSession.setTimeToDo(TimeToDo.TODAY);
                execute(SendMessage
                        .builder()
                        .chatId(chatId)
                        .text("?????????????? ?????????? ?? ?????????????????? ??????????????: HH:mm or HH:mm:ss").build());
                return;
            }

            if (message.getText().contains("no time")) {
                taskService.saveNewTaskToApi(chatId, taskCreateSession.getTaskDTO());
                sendKeyboard(update, "???????????? ?????????????????? ?????? ??????????????!", ReplyKeyboardUtil.getMainKeyboard());
                taskCreateSession = null;
                return;
            }

            if (taskCreateSession.getTimeToDo().compareTo(TimeToDo.ANOTHER_DAY) == 0) {
                String[] timeDateInput = message.getText().split(" ");
                taskCreateSession.setTaskDTO(new TaskDTO(taskCreateSession.getTaskDTO().getTaskName(), new ToDoTime(
                        LocalTime.parse(timeDateInput[1]), LocalDate.parse(timeDateInput[0])
                )));
                taskService.saveNewTaskToApi(chatId, taskCreateSession.getTaskDTO());
                taskCreateSession = null;
                sendKeyboard(update, "???????????? ??????????????????!", ReplyKeyboardUtil.getMainKeyboard());
                return;
            }

            if (taskCreateSession.getTimeToDo().compareTo(TimeToDo.TODAY) == 0) {
                taskCreateSession.setTaskDTO(new TaskDTO(taskCreateSession.getTaskDTO().getTaskName(),
                        new ToDoTime(LocalTime.parse(message.getText()), LocalDate.now())));
                taskService.saveNewTaskToApi(chatId, taskCreateSession.getTaskDTO());
                taskCreateSession = null;
                sendKeyboard(update, "???????????? ??????????????????!", ReplyKeyboardUtil.getMainKeyboard());
                return;
            }
        }
        sendKeyboard(update, "???????????????? ??????????????", ReplyKeyboardUtil.getMainKeyboard());
    }

    @SneakyThrows
    private void sendKeyboard(Update update, String text, ReplyKeyboard keyBoard) {
        execute(new SendMessage().builder().text(text).chatId(update
                            .getMessage()
                            .getChatId())
                    .replyMarkup(keyBoard).build());
    }
}
