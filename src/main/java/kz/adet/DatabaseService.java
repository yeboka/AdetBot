package kz.adet;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseService {

    private static Connection connection;

    public DatabaseService () throws IOException {
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("database_conf.properties");
        Properties properties = new Properties();
        properties.load(inputStream);

        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getTable () throws SQLException {
        Statement statement = connection.createStatement();

        String query = "select * from users";

        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            System.out.println(resultSet.getInt(1));
            System.out.println(resultSet.getString(2));
            System.out.println(resultSet.getString(3));
            System.out.println(resultSet.getString(4));
        }
    }

    public List<Long> getChatIds () throws SQLException {
        List<Long> list = new ArrayList<>();

        PreparedStatement statement = connection.prepareStatement("select chatid from activehabits;");

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            list.add((long) resultSet.getInt("chatid"));
        }

        return list;
    }

    public String getLang(long chatId) throws SQLException {

        PreparedStatement statement = connection.prepareStatement("select language_ from users where chatid = ?;");
        statement.setLong(1, chatId);

        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        String lang = resultSet.getString("language_");

        return lang;
    }

    public String getFirstName(long chatId) throws SQLException {

        PreparedStatement statement = connection.prepareStatement("select firstname from users where chatid = ?;");
        statement.setLong(1, chatId);

        ResultSet resultSet = statement.executeQuery();
        resultSet.next();

        return resultSet.getString("firstname");
    }

    public String getNameOfHabit(long chatId) throws SQLException {

        PreparedStatement statement = connection.prepareStatement("select nameofhabit from activehabits where chatid = ?;");
        statement.setLong(1, chatId);

        ResultSet resultSet = statement.executeQuery();
        resultSet.next();

        return resultSet.getString("nameofhabit");
    }
}
