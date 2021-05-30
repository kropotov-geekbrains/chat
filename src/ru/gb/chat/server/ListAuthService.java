package ru.gb.chat.server;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Artem Kropotov on 24.05.2021
 */
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
    public boolean findByLoginAndNickname(String login, String password, String nickname) {
        for (User u : users)
            if (u.getLogin().equals(login) || u.getNickname().equals(nickname)) {
                return true;
            } else {
                System.out.println("reg");
                save(new User(login, password, nickname));
                return false;
            }
        return true;
    }

    @Override
    public User save(User newUser) {
        users.add(newUser);
        return newUser;
    }

    @Override
    public void remove(User object) {
        users.remove(object);
        System.out.println("del");
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
