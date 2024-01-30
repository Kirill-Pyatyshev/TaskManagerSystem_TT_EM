package com.testtask_effectivemobile.services;


import com.testtask_effectivemobile.dto.AuthUserDto;
import com.testtask_effectivemobile.dto.AuthUserResponceDto;
import com.testtask_effectivemobile.dto.UserDto;
import com.testtask_effectivemobile.exceptions.UserNotFoundException;
import com.testtask_effectivemobile.models.User;
import com.testtask_effectivemobile.models.enums.Role;
import com.testtask_effectivemobile.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public AuthUserResponceDto createUser(User user) throws AuthenticationException, UserNotFoundException {
        String email = user.getEmail();


        if (userRepository.findByEmail(email).orElse(null) != null) {
            throw new UserNotFoundException("Пользователь с email'ом:" + email + " уже существует!");
        }
        System.out.println("1");
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_USER);

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        AuthUserResponceDto userResponce = new AuthUserResponceDto(email, token);

        return userResponce;
    }

    public AuthUserResponceDto login(AuthUserDto authUserDto) throws AuthenticationException, UserNotFoundException {
        String email = authUserDto.getEmail();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, authUserDto.getPassword()));

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Пользователь с Email'ом:" + email + " не найден!"));

        String token = jwtService.generateToken(user);

        return new AuthUserResponceDto(email, token);
    }

    public List<UserDto> all() throws UserNotFoundException {
        if (listUsers().size() == 0) {
            throw new UserNotFoundException("Пользователи не найдены!");
        }
        return usersListToDto(listUsers());
    }

    public UserDto findUserById(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID:" + id + " не найден!"));
        return userToDto(user);
    }

    public void deleteUserById(Long id) throws UserNotFoundException {
        userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID:" + id + " не найден!"));
        userRepository.deleteById(id);
    }

    public void banUserById(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID:" + id + " не найден!"));
        if (user.isActive()) {
            user.setActive(false);
        } else {
            user.setActive(true);
        }
        userRepository.save(user);
    }


    public UserDto userToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    private AuthUserDto userToAuthUserDto(User user) {
        return AuthUserDto.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    private List<UserDto> usersListToDto(List<User> userList) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            userDtoList.add(userToDto(user));
        }
        return userDtoList;
    }
}
