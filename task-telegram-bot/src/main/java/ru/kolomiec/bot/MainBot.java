package ru.kolomiec.bot;

import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.kolomiec.bot.commands.AuthenticationCommand;
import ru.kolomiec.bot.commands.HelpCommand;
import ru.kolomiec.bot.commands.RegistrationCommand;
import ru.kolomiec.bot.commands.StartCommand;

public class MainBot extends TelegramLongPollingCommandBot {

    private final String key = "6116678801:AAEzHaMCHlMbyF56KSTvL5PW8oiIYAdmN78";

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


    @Override
    public void processNonCommandUpdate(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Run /help if you want to get information");
        sendMessage.setChatId(update.getMessage().getChatId());

        try {
            execute(sendMessage);
        } catch (TelegramApiException ex) {
            ex.printStackTrace();
        }
    }
}
