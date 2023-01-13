package kz.adet;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class SwitchingLanguageService {

    static DatabaseService db;
    static {
        try {
            db = new DatabaseService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SwitchingLanguageService() {

    }

    public String getGreeting(long chatid) throws SQLException, IOException {
        InputStream inputStream = SwitchingLanguageService.class.getClassLoader().getResourceAsStream(db.getLang(chatid)+".properties");
        Properties property = new Properties();
        property.load(inputStream);

        return property.getProperty("greeting");
    }

    public String getNotification(long chatid) throws SQLException, IOException {
        InputStream inputStream = SwitchingLanguageService.class.getClassLoader().getResourceAsStream(db.getLang(chatid)+".properties");
        Properties property = new Properties();
        property.load(inputStream);

        return String.format(property.getProperty("notification"), db.getFirstName(chatid), db.getNameOfHabit(chatid));
    }
}
