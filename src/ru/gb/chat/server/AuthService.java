package ru.gb.chat.server;
//Продолжаем изучение...
public interface AuthService {
    User findByLoginAndPassword(String login, String password);
}
