package ru.netology.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor

public class UserInfo implements Serializable {
    private final String login;
    private final String password;
    private final String status;
}
