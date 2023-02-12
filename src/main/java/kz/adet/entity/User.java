package kz.adet.entity;

import kz.adet.DatabaseService;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class User {
    private Long chatId;
    private String userName;
    private String firstName;
    private String lastName;
    private String language_;

    public User(Long chatId, String userName, String firstName, String lastName, String language_) throws IOException, SQLException {
        this.chatId = chatId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.language_ = language_;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLanguage_() {
        return language_;
    }

}
