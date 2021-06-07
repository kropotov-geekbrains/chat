package ru.gb.chat.server;

import java.util.List;


public class DBAuthService implements AuthService, CrudService<User, Long> {
    @Override
    public User findByLoginAndPassword(String login, String password) {
        return null;
    }
    
    @Override
    public User save(User object) {
        return null;
    }
    
    @Override
    public User remove(User object) {
        return null;
    }
    
    @Override
    public User removeById(Long aLong) {
        return null;
    }
    
    @Override
    public User findById(Long aLong) {
        return null;
    }
    
    @Override
    public List<User> findAll() {
        return null;
    }
}
