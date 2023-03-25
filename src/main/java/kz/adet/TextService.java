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