package ru.kolomiec.bot;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.kolomiec.bot.commands.*;
import ru.kolomiec.database.dao.PersonDAO;
import ru.kolomiec.dto.TaskDTO;
import ru.kolomiec.dto.ToDoTime;
import ru.kolomiec.service.TaskService;
import ru.kolomiec.util.ReplyKeyboardUtil;
import ru.kolomiec.util.StringUtil;

import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class MainBot extends TelegramLongPollingCommandBot {

    private final String KEY = "6116678801:AAEzHaMCHlMbyF56KSTvL5PW8oiIYAdmN78";
    private final String BOT_NAME = "Task385Bot";
    private final PersonDAO personDAO = new PersonDAO();
    private final TaskService taskService = new TaskService();
    private TaskCreateSession taskCreateSession = null;
    private boolean isNextMessageIsTask = false;
    private boolean isNextMessageIsTaskDeleting = false;
    public MainBot() {
        register(new StartCommand("/start", "start command"));
        register(new HelpCommand("/help", "shows all commands"));
        register(new RegistrationCommand("/r", "registration on api"));
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
        Message message = null;
        Optional<CallbackQuery> callbackQuery =
                Optional.ofNullable(update.getCallbackQuery());
        if (update.getCallbackQuery() != null) {
            message = update.getCallbackQuery().getMessage();
        }
        if (update.getMessage() != null) {
            message = update.getMessage();
        }
        Long chatId = message.getChatId();
        log.info(String.format("getting message from user with chatId: %s", chatId));
        if (!personDAO.isPersonSavedInDb(chatId)) {
            log.info(String.format(
                    "person chat id: %s is not authorized", chatId
            ));
            execute(new SendMessage().builder().chatId(chatId).text("Вы не аутентифицированы на API").build());
            return;
        }

        if (taskCreateSession == null && !isNextMessageIsTask) {
            if (isNextMessageIsTaskDeleting) {
                String taskId = callbackQuery.orElseThrow(
                        () -> new RuntimeException("callback query is not present")
                ).getData();
                log.info(String.format(
                        "call service method for deleting task from %s task id: %s", chatId, taskId
                ));
                taskService.deleteTask(chatId,
                        StringUtil.getTaskIdFromCallbackData(taskId));
                execute(SendMessage.builder()
                        .chatId(message.getChatId())
                        .text("Задача удалена")
                        .replyMarkup(ReplyKeyboardUtil.getMainKeyboard()).build());
                isNextMessageIsTaskDeleting = false;
                return;
            }
            if (update.getMessage().getText().contains("delete task")) {
                log.info("getting request for deleting task by chat id: " + chatId);
                InlineKeyboardMarkup deleteKeyboard = ReplyKeyboardUtil.getInlineKeyboardForDeletingTask(
                        taskService.getAllTasksFromApi(chatId)
                );
                execute(SendMessage.builder()
                        .chatId(chatId)
                        .text("Выберите задачу которую нужно удалить")
                        .replyMarkup(deleteKeyboard).build());
                this.isNextMessageIsTaskDeleting = true;
                return;
            }
        }
        if (update.getMessage().getText().contains("all tasks")) {
            log.info(String.format(
                    "getting request by chat id: %s for getting all tasks", chatId
            ));
            TaskDTO[] tasks = taskService.getAllTasksFromApi(chatId);
            sendKeyboard(update,"Все ваши задачи: \n%s".formatted(taskService.arrayDTOToString(tasks)), ReplyKeyboardUtil.getMainKeyboard());
            taskCreateSession = null;
            return;
        }

        if (update.getMessage().getText().contains("new task")) {
            log.info(String.format(
                    "getting message for creating new task by %s", chatId
            ));
            taskCreateSession = new TaskCreateSession();
            isNextMessageIsTask = true;
            execute(new SendMessage().builder().chatId(chatId).text("Введите название задачи").build());
            return;
        }

        if (isNextMessageIsTask) {
            log.info(String.format(
                    "create keyboard for chosing time for task for %s", chatId
            ));
            taskCreateSession.setTaskDTO(new TaskDTO(message.getText(), null));
            isNextMessageIsTask = false;
            sendKeyboard(update, "Когда задача должна быть выполнена(вам придет напоминание)", ReplyKeyboardUtil.getChooseTaskTimeKeyboard());
            return;
        }
        if (taskCreateSession != null) {

            if (message.getText().contains("another day")) {

                taskCreateSession.setTimeToDo(TimeToDo.ANOTHER_DAY);
                execute(SendMessage.builder().chatId(chatId).text("Введите дату и время в след формате yyyy-MM-dd HH:mm").build());
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
                log.info(String.format(
                        "creating task with no time for %s", chatId
                ));
                taskService.saveNewTaskToApi(chatId, taskCreateSession.getTaskDTO());
                sendKeyboard(update, "Задача сохранена без времени!", ReplyKeyboardUtil.getMainKeyboard());
                taskCreateSession = null;
                return;
            }

            if (taskCreateSession.getTimeToDo().compareTo(TimeToDo.ANOTHER_DAY) == 0) {
                log.info(String.format(
                        "creating task with another day time for: %s", chatId
                ));
                String[] timeDateInput = message.getText().split(" ");
                taskCreateSession.setTaskDTO(new TaskDTO(taskCreateSession.getTaskDTO().getTaskName(), new ToDoTime(
                        LocalTime.parse(timeDateInput[1]), LocalDate.parse(timeDateInput[0])
                )));
                taskService.saveNewTaskToApi(chatId, taskCreateSession.getTaskDTO());
                taskCreateSession = null;
                sendKeyboard(update, "Задача сохранена!", ReplyKeyboardUtil.getMainKeyboard());
                return;
            }

            if (taskCreateSession.getTimeToDo().compareTo(TimeToDo.TODAY) == 0) {
                log.info(String.format(
                        "creating task with today time for: %s", chatId
                ));
                taskCreateSession.setTaskDTO(new TaskDTO(taskCreateSession.getTaskDTO().getTaskName(),
                        new ToDoTime(LocalTime.parse(message.getText()), LocalDate.now())));
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
