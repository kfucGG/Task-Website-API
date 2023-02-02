package ru.kolomiec;


import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.kolomiec.bot.MainBot;
import ru.kolomiec.database.HibernateConnection;


public class BotLauncher {


    public static void main( String[] args ) throws Exception {
        HibernateConnection.getSessionFactory();
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(new MainBot());
    }

}
