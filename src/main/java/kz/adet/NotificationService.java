package kz.adet;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class NotificationService  {
    private static NotificationService notificationService;
    private final String botToken;
    private final String botUsername;

    private NotificationService (String botToken, String botUsername) {
        this.botToken = botToken;
        this.botUsername = botUsername;
    }

    public static NotificationService getInstance(String botToken, String botUsername) {
        if (notificationService == null)
            notificationService = new NotificationService(botToken, botUsername);

        return notificationService;
    }

    public void startNotificationService () {
        LocalDateTime now = LocalDateTime.now();
        // "15:00"
        TimeEvaluateService timeToSleep = new TimeEvaluateService(17, 25);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        try {
            scheduler.scheduleWithFixedDelay(new BroadCast(botToken, botUsername),
                    timeToSleep.getInitialDelay(now.getLong(ChronoField.MILLI_OF_DAY)),
                    24*60*60*1000, TimeUnit.MILLISECONDS);  //24*60*60*1000 one day in millis
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class BroadCast implements Runnable {   // thread to send notification message for each user
    private final DatabaseService databaseService;
    private final AdetBot bot;
    private SendMessage sendMessage;

    BroadCast (String botToken, String botUsername) throws IOException {
        this.databaseService = DatabaseService.getInstance(); // connect to DB
        this.bot = new AdetBot(botUsername, botToken);
        this.sendMessage = new SendMessage();
    }

    @Override
    public void run() {
        try {
            List<Long> chatIds = databaseService.getChatIds();
            assert chatIds != null;   // assert like return with condition
            for (long chatId:
                    chatIds) {
                sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText(Objects.requireNonNull(SwitchingLanguageService.getNotificationText(chatId)));
                bot.execute(sendMessage);
                System.out.println("notification send!");
            }
        } catch ( SQLException | TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
