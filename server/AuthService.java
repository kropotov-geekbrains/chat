package ru.gb.chat.server;

/**
 * Created by Artem Kropotov on 24.05.2021
 */
public interface AuthService {
    User findByLoginAndPassword(String login, String password);
    User remove(User object);
    User save(User object);
}

