package kz.adet.entity;


public class User {
    private Long chatId;
    private String userName;
    private String firstName;
    private String lastName;
    private String language_;

    public User(Long chatId, String userName, String firstName, String lastName, String language_) {
        this.chatId = chatId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.language_ = language_;
    }

    public User (long chatId, String userName, String language_) {
        this.chatId = chatId;
        this.userName = userName;
        this.language_ = language_;
    }

    public User() {}

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public void setLanguage_(String language) {
        this.language_ = language;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
