package ru.gb.chat.server;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListAuthService implements AuthService, CrudService<User, Long> {
    private static ListAuthService INSTANCE;
    private final CopyOnWriteArrayList<User> users = new CopyOnWriteArrayList<>();
    private ListAuthService() {
        for (int i = 0; i <= 10; i++) {
            users.add(new User("login" + i, "pass" + i, "nick" + i));
        }
    }
    public static ListAuthService getInstance() {
        if (INSTANCE == null) {
            synchronized (ListAuthService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ListAuthService();
                }
            }
        }
        return INSTANCE;
    }
    @Override
    public User findByLoginAndPassword(String login, String password) {
        for (User u : users) {
            if (u.getLogin().equals(login) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }
    
    @Override
    // todo объявил в CrudService, понять и простить))))
    public User save(User object) {
        
        for (User u : users) {
            if (!u.getLogin().equals(object.getLogin()) && !u.getPassword().equals(object.getPassword()) && !u.getNickname().equals(object.getNickname())) {
                users.add(object);
                return object;
            }
        }
        return null;
    }
    
    @Override
    // todo объявил в CrudService, понять и простить)))) Не удаляет(((
    public User remove(User object) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getLogin().equals(object.getLogin()) &&
                    users.get(i).getPassword().equals(object.getPassword()) &&
                    users.get(i).getNickname().equals(object.getNickname()))
            {
                users.remove(object);
                return null;
            }
        }
        return object;
    }
    
    @Override
    public List<User> findAll() {
        return null;
    }
    // todo не надо
    @Override
    public User removeById(Long aLong) {
        return null;
    }
    // todo не надо
    @Override
    public User findById(Long aLong) {
        return null;
    }
}