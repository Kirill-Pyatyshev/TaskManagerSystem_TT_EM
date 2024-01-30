package com.testtask_effectivemobile.controllers;

import com.testtask_effectivemobile.exceptions.UserNotFoundException;
import com.testtask_effectivemobile.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/all")
    public ResponseEntity<?> all() {
        try{
            return ResponseEntity.ok(userService.all());
        }catch (UserNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Непредвиденная ошибка!\n" + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try{
            return ResponseEntity.ok(userService.findUserById(id));
        }catch (UserNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Непредвиденная ошибка!\n" + e.getMessage());
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id){
        try{
            userService.deleteUserById(id);
            return ResponseEntity.ok("Пользователь с ID:" + id + " удален!");
        }catch (UserNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Непредвиденная ошибка!\n" + e.getMessage());
        }
    }

    @PostMapping("/ban/{id}")
    public ResponseEntity<?> banUserById(@PathVariable Long id){
        try{
            userService.banUserById(id);
            return ResponseEntity.ok("Пользователь с ID:" + id + " забанен!");
        }catch (UserNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Непредвиденная ошибка!\n" + e.getMessage());
        }
    }

}
