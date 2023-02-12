package kz.adet;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.sql.SQLException;

import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
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

        SendMessage response1 = new SendMessage();

        String lan = "kz";

        if(update.hasCallbackQuery()){
            CallbackQuery callbackQuery =  update.getCallbackQuery();
            String data = callbackQuery.getData();
            Long chatId = callbackQuery.getMessage().getChatId();
            String userName = callbackQuery.getFrom().getUserName();

            Message messageFromData = callbackQuery.getMessage();
            if(data.equals("ru")){
                response1.setText("Ты выбрал русский язык!");
                response1.setParseMode("MarkDown");
                response1.setChatId(messageFromData.getChatId());
                lan = "ru";

                try {
                    execute(response1);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            if(data.equals("kz")){
                lan = "kz";
                response1.setText("Сен қазақ тілін таңдадын!");
                response1.setParseMode("MarkDown");
                response1.setChatId(messageFromData.getChatId());


                try {
                    execute(response1);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            Users user = new Users();
            user.setUsername(userName);
            user.setChatId(chatId);
            user.setLanguage(lan);
            try {
                DatabaseService.getInstance().addUsers(user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String command = update.getMessage().getText();


        if (command.equals("/start")) {
            String message = " Salem!Senimen birge adet_bot.Men sagan zhana adet kalyptastyruga komektesemin!Esimindi ter";
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


    }

}





