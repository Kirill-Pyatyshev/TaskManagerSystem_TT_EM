package com.testtask_effectivemobile.controllers;


import com.fasterxml.jackson.annotation.*;
import com.testtask_effectivemobile.exceptions.NoAccessException;
import com.testtask_effectivemobile.exceptions.TaskNotFoundException;
import com.testtask_effectivemobile.exceptions.UserNotFoundException;
import com.testtask_effectivemobile.models.Task;
import com.testtask_effectivemobile.models.User;
import com.testtask_effectivemobile.utils.Views;
import com.testtask_effectivemobile.models.enums.TaskStatus;
import com.testtask_effectivemobile.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;


    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody Task task, @AuthenticationPrincipal User user) {
        try {
            return ResponseEntity.ok().body(taskService.createTask(task, user));

        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Непредвиденная ошибка!\n" + e.getMessage());
        }
    }

    @JsonView(Views.PublicLow.class)
    @GetMapping("/comment/{task_id}")
    public ResponseEntity<?> commentTask(@PathVariable Long task_id, @RequestParam(name = "comment") String content, @AuthenticationPrincipal User user ) {
        try {
            return ResponseEntity.ok().body(taskService.leaveComment(task_id,content, user));
        }catch (TaskNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Непредвиденная ошибка!\n" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id){
        try{
            taskService.deleteTaskById(id);
            return ResponseEntity.ok("Задача с ID:" + id + " удалена!");
        }catch (TaskNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Непредвиденная ошибка!\n" + e.getMessage());
        }
    }

    @JsonView(Views.PublicLow.class)
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try{
            return ResponseEntity.ok(taskService.findTaskById(id));
        }catch (TaskNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Непредвиденная ошибка!\n" + e.getMessage());
        }
    }

    @JsonView(Views.PublicLow.class)
    @GetMapping(value = "/all")
    public ResponseEntity<?> all() {
        try{
            return ResponseEntity.ok(taskService.all());
        }catch (TaskNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Непредвиденная ошибка!\n" + e.getMessage());
        }
    }

    @JsonView(Views.PublicLow.class)
    @GetMapping(value = "/all/my")
    public ResponseEntity<?> allMyTasks(@AuthenticationPrincipal User user) {
        try{
            return ResponseEntity.ok(taskService.allMyTask(user));
        }catch (TaskNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Непредвиденная ошибка!\n" + e.getMessage());
        }
    }

    @JsonView(Views.PublicLow.class)
    @GetMapping(value = "/search/author")
    public ResponseEntity<?> searchByAuthor(@RequestParam(name = "name") String name) {
        try{
            return ResponseEntity.ok(taskService.searchByAuthor(name));
        }catch (TaskNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Непредвиденная ошибка!\n" + e.getMessage());
        }
    }

    @JsonView(Views.PublicLow.class)
    @GetMapping(value = "/search/executor")
    public ResponseEntity<?> searchByExecutor(@RequestParam String name) {
        try{
            return ResponseEntity.ok(taskService.searchByExecutor(name));
        }catch (TaskNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Непредвиденная ошибка!\n" + e.getMessage());
        }
    }

    @JsonView(Views.Public.class)
    @GetMapping(value = "/{task_id}/assign/{executor_id}")
    public ResponseEntity<?> assignTask(@PathVariable Long task_id, @PathVariable Long executor_id,
                                        @AuthenticationPrincipal User user) {
        try{
            return ResponseEntity.ok(taskService.assignTaskToExecutor(task_id,executor_id, user));
        }catch (TaskNotFoundException | NoAccessException | UserNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Непредвиденная ошибка!\n" + e.getMessage());
        }
    }

    @JsonView(Views.PublicLow.class)
    @GetMapping(value = "/change/status/{task_id}")
    public ResponseEntity<?> changeStatusTaskByExecutor(@PathVariable Long task_id,
                                                        @RequestParam(name = "status") TaskStatus status,
                                                        @AuthenticationPrincipal User user) {
        try{
            return ResponseEntity.ok(taskService.changeStatusTaskByExecutor(task_id,status, user));
        }catch (TaskNotFoundException | NoAccessException | UserNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Непредвиденная ошибка!\n" + e.getMessage());
        }
    }

    @JsonView(Views.PublicLow.class)
    @GetMapping(value = "/edit/{id}")
    public ResponseEntity<?> editTask(@PathVariable Long id, @RequestBody Task task,
                                      @AuthenticationPrincipal User user) {
        try{
            return ResponseEntity.ok(taskService.updateTask(id,task, user));
        }catch (TaskNotFoundException | NoAccessException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Непредвиденная ошибка!\n" + e.getMessage());
        }
    }


}
