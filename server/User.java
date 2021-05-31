package ru.gb.chat.server;

/**
 * Created by Artem Kropotov on 24.05.2021
 */
public class User {
    private final String login;
    private final String password;
    private final String nickname;

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public User(String login, String password, String nickname) {
        this.login = login;
        this.password = password;
        this.nickname = nickname;
    }
}
