package kz.adet;

import kz.adet.entity.User;
import lombok.SneakyThrows;import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.sql.SQLException;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Objects;

public class AdetBot extends TelegramLongPollingBot {

    private String botUsername;
    private String botToken;
    User user;


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

    @SneakyThrows@Override
    public void onUpdateReceived(Update update) {


        DatabaseService databaseService = null;
        try {
            databaseService = DatabaseService.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (update.hasCallbackQuery()) {
            String callData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callData.equals("done")) {
                user = new User(update.getCallbackQuery().getFrom().getId(),
                        update.getCallbackQuery().getFrom().getUserName(),
                        update.getCallbackQuery().getFrom().getFirstName(),
                        update.getCallbackQuery().getFrom().getLastName(),
                        update.getCallbackQuery().getFrom().getLanguageCode());
                String answer = Objects.requireNonNull(TextService.getInstance().getMessageText(user, TextCascade.HABIT_COMPLETED_TEXT));
                EditMessageText editing = new EditMessageText();
                editing.setChatId(chatId);
                editing.setMessageId((int) (messageId));
                editing.setText(answer);

                try {
                    execute(editing);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        } else if (update.getMessage().hasText()) {
            if (update.getMessage().getText().equals("/new_habit")) {
                Long chatId = update.getMessage().getChatId();
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId.toString());
                try {
                    if (!databaseService.validHabit(chatId)) {
                        sendMessage.setText(TextService.getInstance().getMessageText(user, TextCascade.FINISHING_HABIT));
                    } else {
                        sendMessage.setText(TextService.getInstance().getMessageText(user, TextCascade.ADD_HABIT));
                    }
                    execute(sendMessage);
                } catch (SQLException | TelegramApiException | IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (update.getMessage().getText().equals("/start")) {
                user = new User(update.getMessage().getChatId(),
                        update.getMessage().getFrom().getUserName(),
                        update.getMessage().getFrom().getFirstName(),
                        update.getMessage().getFrom().getLastName(),
                        update.getMessage().getFrom().getLanguageCode());

                databaseService.addUser(user);

                try {
                    SendMessage message = new SendMessage();
                    message.setChatId(user.getChatId());
                    message.setText(TextService.getInstance().getMessageText(user, TextCascade.GREETING));
                    System.out.println(message.getText());
                    execute(message);
                } catch (SQLException | IOException | TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Long chatId = update.getMessage().getChatId();
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId.toString());
                try {
                    databaseService.addNewHabit(chatId, update.getMessage().getText());
                    sendMessage.setText(TextService.getInstance().getMessageText(user, TextCascade.HABIT_ADDED));

                    execute(sendMessage);
                } catch (SQLException | TelegramApiException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}





