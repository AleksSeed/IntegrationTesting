package ru.skypro.lessons.springboot.springboot.dto;

import org.springframework.stereotype.Component;
import ru.skypro.lessons.springboot.springboot.pojo.User;

@Component
public class UserMapper {
    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setLogin(user.getLogin());
        userDto.setPassword(user.getPassword());
        userDto.setRole(user.getRole());
        return userDto;
    }
}
