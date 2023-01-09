package kz.adet;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class AdetBot extends TelegramLongPollingBot {

    private String botUsername;
    private String botToken;

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
        System.out.println(update.getMessage().getText());
        System.out.println(update.getMessage().getFrom().getFirstName());

        String usersMessage = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Hi i'm Adet Bot!");
        try {
            switch (usersMessage) {
                case "/start" :
                    execute(sendMessage);
                    break;

            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
