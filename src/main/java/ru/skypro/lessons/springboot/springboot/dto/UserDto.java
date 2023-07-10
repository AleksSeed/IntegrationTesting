package ru.skypro.lessons.springboot.springboot.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.skypro.lessons.springboot.springboot.pojo.Role;

@Component
@Getter
@Setter
public class UserDto {
    private int id;
    private String login;
    private String password;
    private Role role;
}
