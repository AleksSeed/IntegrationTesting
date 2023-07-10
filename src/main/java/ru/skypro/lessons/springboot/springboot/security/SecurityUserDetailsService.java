package ru.skypro.lessons.springboot.springboot.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skypro.lessons.springboot.springboot.dto.UserDto;
import ru.skypro.lessons.springboot.springboot.repository.UserRepository;
import ru.skypro.lessons.springboot.springboot.service.UserMapper;

@Service
public class SecurityUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SecurityUserPrincipal userDetails;

    public SecurityUserDetailsService(UserRepository userRepository, UserMapper userMapper, SecurityUserPrincipal userDetails) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userDetails = userDetails;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserDto userDto = userRepository.findByLogin(username)
                .map(userMapper::toDto)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Имя пользователя %s  не найдено".formatted(username))
                );
        userDetails.setUserDto(userDto);
        return userDetails;
    }
}