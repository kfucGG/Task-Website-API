package ru.kolomiec;


import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.kolomiec.bot.MainBot;
import ru.kolomiec.database.HibernateConnection;
import ru.kolomiec.database.LiquibaseMigration;
import ru.kolomiec.service.TaskRemindService;


public class BotLauncher {


    public static void main( String[] args ) throws Exception {
        new LiquibaseMigration().doMigration();
        HibernateConnection.getSessionFactory();
        new TelegramBotsApi(DefaultBotSession.class).registerBot(new MainBot());
        new TaskRemindService(new MainBot()).start();
    }

}
