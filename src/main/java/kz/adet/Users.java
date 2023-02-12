package kz.adet;

public class Users {
    private Long chatId;
    private String username;
    private String language;


    public Users(Long chatId, String username,String language) {
        this.chatId = chatId;
       this.username = username;
       this.language = language;
    }

    public Users() {

    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public String getLanguage() {
        return language;
    }


}
