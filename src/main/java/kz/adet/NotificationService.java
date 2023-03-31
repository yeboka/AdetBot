package kz.adet;

import kz.adet.entity.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class NotificationService {
    private static NotificationService notificationService;
    private final String botToken;
    private final String botUsername;

    private NotificationService(String botToken, String botUsername) {
        this.botToken = botToken;
        this.botUsername = botUsername;
    }

    public static NotificationService getInstance(String botToken, String botUsername) {
        if (notificationService == null)
            notificationService = new NotificationService(botToken, botUsername);

        return notificationService;
    }

    public void startNotificationService() {
        LocalDateTime now = LocalDateTime.now();
        TimeEvaluateService timeToSleep = new TimeEvaluateService(23, 13);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        try {
            scheduler.scheduleWithFixedDelay(new BroadCast(botToken, botUsername),
                    timeToSleep.getInitialDelay(now.getLong(ChronoField.MILLI_OF_DAY)),
                    24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS);  //24*60*60*1000 one day in millis
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class BroadCast implements Runnable {   // thread to send notification message for each user
    private final DatabaseService databaseService;
    private final AdetBot bot;
    private SendMessage sendMessage;

    BroadCast(String botToken, String botUsername) throws IOException {
        this.databaseService = DatabaseService.getInstance(); // connect to DB
        this.bot = new AdetBot(botUsername, botToken);
        this.sendMessage = new SendMessage();
    }

    @Override
    public void run() {
        try {
            List<User> users = databaseService.getUsers();
            assert users != null;   // assert like return with condition
            for (User user :
                    users) {
                sendMessage = new SendMessage();
                sendMessage.setChatId(user.getChatId());
                sendMessage.setText(Objects.requireNonNull(TextService.getInstance().getMessageText(user, TextCascade.NOTIFICATION)));
                sendMessage.setReplyMarkup(getInlineKeyboard());
                bot.execute(sendMessage);
            }
        } catch (SQLException | TelegramApiException | IOException e) {
            e.printStackTrace();
        }
    }

    public InlineKeyboardMarkup getInlineKeyboard() {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("DONE");
        button.setCallbackData("done");

        rowInline.add(button);
        rowsInline.add(rowInline);
        markup.setKeyboard(rowsInline);

        return markup;
    }
}
