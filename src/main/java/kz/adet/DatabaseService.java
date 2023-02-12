package kz.adet;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseService {
    private static Connection connection;
    private static DatabaseService databaseService;

    private DatabaseService () throws IOException {
        Properties properties = getProperties();
        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseService getInstance() throws IOException {
        if (databaseService != null) return databaseService;
        else return databaseService = new DatabaseService();
    }

    public Properties getProperties() throws IOException {
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("database_conf.properties");
        Properties properties = new Properties();
        properties.load(inputStream);

        return properties;
    }

    public List<Long> getChatIds() throws SQLException {
        List<Long> list = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("select chatid from activehabits;");
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            list.add((long) resultSet.getInt("chatid"));
        }
        return list;
    }

    public User getUser(long chatid) throws SQLException, IOException {
        PreparedStatement statement = connection.prepareStatement("select * from users where chatid = ?");
        statement.setLong(1, chatid);
        ResultSet resultSet = statement.executeQuery();

        User user = null;
        while (resultSet.next()){
            user =  new User(
                    resultSet.getLong("chatid"),
                    resultSet.getString("username"),
                    resultSet.getString("firstname"),
                    resultSet.getString("lastname"),
                    resultSet.getString("language_")
            );
        }
        return user;
    }

    public String getLang(long chatId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select language_ from users where chatid = ?;");
        statement.setLong(1, chatId);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();

        return resultSet.getString("language_");

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



    public void addUsers(Users user){
        try{
        PreparedStatement statement = connection.prepareStatement(""  + "INSERT INTO users (chatid,username,language_)" + "VALUES (?,?,?)");
        statement.setLong(1,user.getChatId());
        statement.setString(2,user.getUsername());
        statement.setString(3,user.getLanguage());
        statement.executeUpdate();
        statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
