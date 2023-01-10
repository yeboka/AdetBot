package kz.adet;

import java.sql.*;

public class DatabaseService {
    private static final String URL = "jdbc:postgresql://localhost:49153/adet";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgrespw";

    private static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void getTable () throws SQLException {
        Statement statement = connection.createStatement();

        String queri = "select * from users";

        ResultSet resultSet = statement.executeQuery(queri);

        while (resultSet.next()) {
            System.out.println(resultSet.getInt(1));
            System.out.println(resultSet.getString(2));
            System.out.println(resultSet.getString(3));
            System.out.println(resultSet.getString(4));
        }
    }
}
