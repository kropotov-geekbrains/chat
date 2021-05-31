package ru.gb.chat.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Artem Kropotov on 24.05.2021
 */
@AllArgsConstructor
@Getter
@ToString
public class User {

    private final String login;
    private final String password;
    private final String nickname;


}
