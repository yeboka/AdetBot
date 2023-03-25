package kz.adet;

import kz.adet.entity.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.sql.SQLException;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public class AdetBot extends TelegramLongPollingBot {

    private String botUsername;
    private String botToken;


    public AdetBot(String botUsername, String botToken) {
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


        DatabaseService databaseService = null;
        try {
            databaseService = DatabaseService.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(update.getMessage().hasText()) {
             if (update.getMessage().getText().equals("/new_habit")) {
                Long chatId = update.getMessage().getChatId();
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId.toString());
                try {
                    if(!databaseService.validHabit(chatId)){
                        sendMessage.setText("Sende ali byryngy adetin bitpedi");
                    }
                    else {
                        String message = "Zhana adetindi engiz";
                        sendMessage.setText(message);
                    }
                    execute(sendMessage);
                } catch (SQLException | TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
            else if (update.getMessage().getText().equals("/start")){
                 User user = new User(update.getMessage().getChatId(),
                         update.getMessage().getFrom().getUserName(),
                         update.getMessage().getFrom().getFirstName(),
                         update.getMessage().getFrom().getLastName(),
                         update.getMessage().getFrom().getLanguageCode());

                 databaseService.addUser(user);

                 try {
                     SendMessage message = new SendMessage();
                     message.setChatId(user.getChatId());
                     message.setText(TextService.getInstance().getMessageText(user, TextCascade.GREETING));

                     execute(message);
                 } catch (SQLException | IOException | TelegramApiException e) {
                     throw new RuntimeException(e);
                 }
             }
            else {
                Long chatId = update.getMessage().getChatId();
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId.toString());
                try {
                    databaseService.addNewHabit(chatId, update.getMessage().getText());
                    sendMessage.setText("Adetin satti engizildi");
                    execute(sendMessage);
                } catch (SQLException | TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}





