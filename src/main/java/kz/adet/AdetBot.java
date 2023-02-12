package kz.adet;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.sql.SQLException;


public class AdetBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final String botToken;

    public AdetBot (String botUsername, String botToken) {
        this.botUsername = botUsername;
        this.botToken = botToken;
    }


    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        Long chatId = update.getMessage().getChatId();
        sendMessage.setChatId(chatId.toString());
        DatabaseService databaseService = null;
        try {
            databaseService = DatabaseService.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(update.hasMessage() && update.getMessage().hasText()) {
            if (update.getMessage().getText().equals("/new_habit")) {
                try {
                    if(!databaseService.validHabit(chatId)){
                        sendMessage.setText("Sende ali byryngy adetin bitpedi");
                    }
                    else {
                        String message = "Zhana adetindi engiz";
                        sendMessage.setText(message);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    databaseService.addNewHabit(chatId, update.getMessage().getText());
                    sendMessage.setText("Adetin satti engizildi");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
