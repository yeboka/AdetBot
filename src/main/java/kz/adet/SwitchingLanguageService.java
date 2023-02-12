package kz.adet;

import com.vdurmont.emoji.EmojiParser;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class SwitchingLanguageService {

    static DatabaseService db;
    static {
        try {
            db = DatabaseService.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SwitchingLanguageService() {

    }

    public static String getGreeting(long chatid) throws SQLException, IOException {
        InputStream inputStream = SwitchingLanguageService.class.getClassLoader().getResourceAsStream(db.getLang(chatid)+".properties");
        Properties property = new Properties();
        property.load(inputStream);

        return property.getProperty("greeting");
    }

    public static String getNotificationText(long chatid) throws SQLException {
        String alarmEmoji = EmojiParser.parseToUnicode(":alarm_clock:");
        InputStream inputStream = SwitchingLanguageService.class.getClassLoader().getResourceAsStream(db.getLang(chatid)+".properties");
        Properties property = new Properties();
        try {
            property.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return String.format(property.getProperty("notification"),alarmEmoji , db.getFirstName(chatid), db.getNameOfHabit(chatid));
    }
}