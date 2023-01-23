package kz.adet;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.List;

public class NotificationService extends Thread {

    private final String botUsername;
    private final String botToken;

    public NotificationService(String botToken, String botUsername) {
        this.botUsername = botUsername;
        this.botToken = botToken;
    }

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            LocalDateTime now = LocalDateTime.now();
            // "15:00"
            TimeEvaluateService timeToSleep = new TimeEvaluateService(15, 0);
            Thread.sleep(timeToSleep.getMillisToSleep(now.getLong(ChronoField.MILLI_OF_DAY)));

            DatabaseService databaseService = new DatabaseService();
            List<Long> chatIds = databaseService.getChatIds();

            SendMessage sendMessage;
            AdetBot bot = new AdetBot(botUsername, botToken);

            for (long chatId:
                    chatIds) {
                sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText(SwitchingLanguageService.getNotification(chatId));

                bot.execute(sendMessage);
            }
        }
    }
}
