package ru.kolomiec.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.kolomiec.database.dao.PersonDAO;
import ru.kolomiec.database.entity.Person;
import ru.kolomiec.dto.TaskDTO;
import ru.kolomiec.kafka.TaskKafkaConsumerConfig;

import java.time.Duration;

@Slf4j
public class TaskRemindService extends Thread {

    private final KafkaConsumer<String, String> kafkaConsumer = new TaskKafkaConsumerConfig().getTaskKafkaConsumer();
    private final PersonDAO personDAO = new PersonDAO();
    private final TelegramLongPollingBot bot;
    public TaskRemindService(TelegramLongPollingBot bot) {
        super("Task Remind Service Thread");
        this.bot = bot;
    }
    @Override
    public void run() {
        log.info("Thread is started polling kafka messages from topic every 100 millis");
        while(true){
            ConsumerRecords<String, String> records =
                    kafkaConsumer.poll(Duration.ofMillis(100));
            records.forEach(a -> {
                System.out.println("gettings task " + a.value());
                Person ownerOfTask = personDAO.findPersonByUsername(a.key());
                sendTaskWhichTimeIsCurrentTime(ownerOfTask.getChatId(), a.value());
            });
        }
    }

    private void sendTaskWhichTimeIsCurrentTime(Long chatId, String text) {
        trySendRemindTaskMessage(generateTaskRemindMessage(chatId, text));
    }
    private void trySendRemindTaskMessage(SendMessage sendMessage) {
        try {
            this.bot.execute(sendMessage);
        } catch (TelegramApiException ex) {
            ex.printStackTrace();
        }
    }
    private SendMessage generateTaskRemindMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }
}
