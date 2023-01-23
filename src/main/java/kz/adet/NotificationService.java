package kz.adet;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Objects;

public class NotificationService extends Thread {

    private final DatabaseService databaseService;
    private final AdetBot bot;
    private SendMessage sendMessage;

    public NotificationService(String botToken, String botUsername) throws IOException {
        databaseService = DatabaseService.getInstance(); // connect to DB
        bot = new AdetBot(botUsername, botToken);
        sendMessage = new SendMessage();
    }
    // TODO refactor with using ScheduledExecutor
    @Override
    public void run() {
        while (true) {
            LocalDateTime now = LocalDateTime.now();
            // "15:00"
            TimeEvaluateService timeToSleep = new TimeEvaluateService(15, 0);
            try {
                Thread.sleep(timeToSleep.getMillisToSleep(now.getLong(ChronoField.MILLI_OF_DAY)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            try {
                List<Long> chatIds = databaseService.getChatIds();
                assert chatIds != null;   // assert like return with condition
                for (long chatId:
                        chatIds) {
                    sendMessage = new SendMessage();
                    sendMessage.setChatId(chatId);
                    sendMessage.setText(Objects.requireNonNull(SwitchingLanguageService.getNotificationText(chatId)));
                    bot.execute(sendMessage);
                }
            } catch ( SQLException | TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
