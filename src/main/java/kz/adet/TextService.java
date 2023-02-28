package kz.adet;

import kz.adet.entity.User;
import lombok.Getter;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

// TODO: 26.01.2023 need to change name
public class TextService {
    private final Properties textProperties;

    public TextService() throws IOException {
        this.textProperties = new Properties();
        InputStream inputStream = TextService.class.getClassLoader().getResourceAsStream("text.properties");
        this.textProperties.load(inputStream);
    }

    /**
     * Get text using user information and text cascade.
     */

    //TODO receive as param User object instead of database,
    // then in param this method should receive User and TextCascade
    public String getTextWithUser(DatabaseService db, long chatId, TextCascade cascade) throws SQLException, IOException {
        String text = textProperties.getProperty(String.format("%s.%s", db.getUser(chatId).getLanguage_(), cascade.getKeyText()));
        if (text == null) {
            return null;
        }
        text = text.replaceAll("<NAME>", db.getUser(chatId).getFirstName());

        return text;
    }
}

enum TextCascade {
    NOTIFICATION("notificationText"),
    GREETING("greetingText"),
    ADD_HABIT("addHabitText"),
    HABIT_COMPLETED_TEXT("habitCompletedText"),
    HABIT_UNCOMPLETED_TEXT("habitUncompletedText");

    @Getter
    private final String keyText;

    TextCascade(String greetingText) {
        keyText = greetingText;
    }
}