package com.testtask_effectivemobile.controllers;


import com.testtask_effectivemobile.dto.AuthUserDto;
import com.testtask_effectivemobile.exceptions.UserNotFoundException;
import com.testtask_effectivemobile.models.User;
import com.testtask_effectivemobile.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> registration(@RequestBody User user) {
        try {
            return ResponseEntity.ok().body(userService.createUser(user));

        }catch (AuthenticationException e){
            return ResponseEntity.badRequest().body("Invalid email or password");
        } catch (UserNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Непредвиденная ошибка!\n" + e.getMessage());
        }
    }

    @PostMapping ("/sign-in")
    public ResponseEntity<?> login(@RequestBody AuthUserDto authUserDto){
        try {
            return ResponseEntity.ok(userService.login(authUserDto));
        }catch (AuthenticationException e){
            return ResponseEntity.badRequest().body("Invalid email or password");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Непредвиденная ошибка!\n" + e.getMessage());
        }
    }
}
