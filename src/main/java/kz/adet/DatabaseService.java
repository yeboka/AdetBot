package kz.adet;

import kz.adet.entity.User;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseService {

    private Connection connection;
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

    public List<User> getUsers() throws SQLException {
        ArrayList<User> users = new ArrayList<>();

        PreparedStatement statement = connection.prepareStatement("select users.* from activehabits, users where users.chatid = activehabits.chatid");
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()){
            User user = new User();
            user.setChatId(resultSet.getLong("chatid"));
            user.setUserName(resultSet.getString("username"));
            user.setFirstName(resultSet.getString("firstname"));
            user.setLanguage_(resultSet.getString("language_"));
            users.add(user);
        }

        return users;
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

    public boolean validHabit(long chatId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select * from activehabits where chatid = ?;");
        statement.setLong(1, chatId);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        if(resultSet.getRow() != 0)
            return false;
        return true;
    }

    public void addNewHabit(long ch, String nameofHabit) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("" + "insert into activehabits (chatid, nameofhabit, story)" +"values (?, ?, ?)");
        statement.setLong(1,ch);
        statement.setString(2,nameofHabit);
        statement.setString(3,"");
        statement.executeUpdate();
        statement.close();

    }

    public void addUser(User user){
        try{
        PreparedStatement statement = connection.prepareStatement(""  + "INSERT INTO users (chatid,username, firstname, lastname, language_)" + "VALUES (?,?,?,?,?)");
        statement.setLong(1,user.getChatId());
        statement.setString(2,user.getUserName());
        statement.setString(3,user.getFirstName());
        statement.setString(4,user.getLastName());
        statement.setString(5,user.getLanguage_());
        statement.executeUpdate();
        statement.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
