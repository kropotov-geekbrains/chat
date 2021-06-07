package ru.gb.chat.server;

public interface AuthService {
    User findByLoginAndPassword(String login, String password);
    
    User save(User user);
    
    User remove(User user);
}