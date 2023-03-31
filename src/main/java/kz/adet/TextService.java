package kz.adet;

import kz.adet.entity.User;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class TextService {
    private final Properties textProperties;
    private static TextService textService;

    private TextService() throws IOException {
        this.textProperties = new Properties();
        InputStream inputStream = TextService.class.getClassLoader().getResourceAsStream("text.properties");
        this.textProperties.load(inputStream);
    }

    public static TextService getInstance() throws IOException {
        if (textService == null)
            textService = new TextService();

        return textService;
    }

    /**
     * Get text using user information and text cascade.
     */

    public String getMessageText(User user, TextCascade cascade) throws SQLException, IOException {
        String text = textProperties.getProperty(String.format("%s.%s", user.getLanguage_(), cascade.getKeyText()));
        if (text == null) {
            return null;
        }
        text = text.replaceAll("<NAME>", user.getFirstName());

        if (user.getLastName() != null){
            text = text.replaceAll("<SURNAME>", user.getLastName());
        }
        else {
            text = text.replaceAll("<SURNAME>", "");
        }

        return text;
    }
}

enum TextCascade {
    NOTIFICATION("notificationText"),
    GREETING("greetingText"),
    HABIT_ADDED("habitAddedText"),
    HABIT_COMPLETED_TEXT("habitCompletedText"),
    HABIT_UNCOMPLETED_TEXT("habitUncompletedText"),
    ADD_HABIT("addHabitText"),
    FINISHING_HABIT("finishingHabit");

    @Getter
    private final String keyText;

    TextCascade(String text) {
        keyText = text;
    }
}