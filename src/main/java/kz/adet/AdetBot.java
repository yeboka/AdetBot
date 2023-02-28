package kz.adet;

import kz.adet.entity.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.sql.SQLException;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        if((update.hasMessage() && update.getMessage().hasText()) || update.hasCallbackQuery()) {
            if (update.hasCallbackQuery()) {
                SendMessage response1 = new SendMessage();
                String language = "kz";
                CallbackQuery callbackQuery =  update.getCallbackQuery();
                String data = callbackQuery.getData();
                Long chatId = callbackQuery.getMessage().getChatId();
                String userName = callbackQuery.getFrom().getUserName();
                System.out.println(data);
                if(data.equals("ru")){
                    response1.setText("Ты выбрал русский язык!");
                    response1.setChatId(chatId);
                    language = "ru";

                    try {
                        execute(response1);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                if(data.equals("kz")){
                    language = "kz";
                    response1.setText("You choose kz!");
                    response1.setChatId(chatId);
                    try {
                        execute(response1);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }

                User user = new User(chatId, userName, language);

                try {
                    DatabaseService.getInstance().addUser(user);
                    System.out.println("User added");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (update.getMessage().getText().equals("/new_habit")) {
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
            //TODO instead of inline keyboard get language from telegram setting of user
            // update.getMessage().getFrom().getLanguageCode()
            else if (update.getMessage().getText().equals("/start")){
                String message = " Salem!Senimen birge adet_bot. Men sagan zhana adet kalyptastyruga komektesemin!Esimindi ter";
                SendMessage response = new SendMessage();
                response.setChatId(update.getMessage().getChatId().toString());
                response.setText(message);

                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

                List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                List<InlineKeyboardButton> buttonsList = new ArrayList<>();
                InlineKeyboardButton button1 = new InlineKeyboardButton();
                InlineKeyboardButton button2 = new InlineKeyboardButton();
                buttonsList.add(button1);
                buttonsList.add(button2);
                buttons.add(buttonsList);
                button1.setText("Kazakh");
                button2.setText("Russian");
                button1.setCallbackData("kz");
                button2.setCallbackData("ru");
                inlineKeyboardMarkup.setKeyboard(buttons);
                response.setReplyMarkup(inlineKeyboardMarkup);

                try {
                    execute(response);
                } catch (TelegramApiException E) {
                    E.printStackTrace();
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





