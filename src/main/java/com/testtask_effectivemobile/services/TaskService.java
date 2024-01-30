package com.testtask_effectivemobile.services;

import com.testtask_effectivemobile.dto.AuthUserDto;
import com.testtask_effectivemobile.dto.CommentDto;
import com.testtask_effectivemobile.exceptions.NoAccessException;
import com.testtask_effectivemobile.exceptions.TaskNotFoundException;
import com.testtask_effectivemobile.exceptions.UserNotFoundException;
import com.testtask_effectivemobile.models.Comment;
import com.testtask_effectivemobile.models.Task;
import com.testtask_effectivemobile.models.User;
import com.testtask_effectivemobile.models.enums.TaskStatus;
import com.testtask_effectivemobile.repositories.CommentRepository;
import com.testtask_effectivemobile.repositories.TaskRepository;
import com.testtask_effectivemobile.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public List<Task> listTasks() {
        return taskRepository.findAll();
    }


    public boolean createTask(Task task, User user) {
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setAuthor(user);
        taskRepository.save(task);
        return true;
    }

    public void deleteTaskById(Long id) throws TaskNotFoundException {
        taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Задача с ID:" + id + " не найдена!"));
        taskRepository.deleteById(id);
    }

    public Task findTaskById(Long id) throws TaskNotFoundException {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Задача с ID:" + id + " не найдена!"));
    }

    public List<Task> all() throws TaskNotFoundException {
        if (listTasks().size() == 0) {
            throw new TaskNotFoundException("Список задач пуст!");
        }
        return listTasks();
    }

    public List<Task> allMyTask(User user) throws TaskNotFoundException {

        List<Task> myTasks = taskRepository.findTasksByAuthor(user);

        if (myTasks.size() == 0) {
            throw new TaskNotFoundException("Список ваших задач пуст!");
        }
        return myTasks;
    }

    public Task assignTaskToExecutor(Long task_id, Long executor_id, User user) throws TaskNotFoundException, NoAccessException, UserNotFoundException {
        Task task = taskRepository.findById(task_id)
                .orElseThrow(() -> new TaskNotFoundException("Задача с ID:" + task_id + " не найдена!"));

        if (!task.getAuthor().getEmail().equals(user.getEmail())) {
            throw new NoAccessException("Нет доступа! Вы не являетесь автором данной задачи!");
        }

        User newExecutor = userRepository.findById(executor_id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID: " + executor_id + " для выполнения задачи не найдено!"));

        task.setExecutor(newExecutor);
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task task, User user) throws TaskNotFoundException, NoAccessException, IllegalAccessException {
        Task taskTemp = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Задача с ID:" + id + " не найдена!"));

        if (!taskTemp.getAuthor().getEmail().equals(user.getEmail())) {
            throw new NoAccessException("Нет доступа! Вы не являетесь автором данной задачи!");
        }

        Field[] fields = task.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.getName().equals("header") || field.getName().equals("description") ||
                    field.getName().equals("status") || field.getName().equals("priority")) {
                field.setAccessible(true);
                Object value = field.get(task);
                if (value != null) {
                    field.set(taskTemp, value);
                }
            }
        }
        return taskRepository.save(taskTemp);
    }

    public List<Task> searchByAuthor(String name) throws TaskNotFoundException, UserNotFoundException {

        User user = userRepository.findByName(name).orElseThrow(() -> new UserNotFoundException("Пользователь с именем:" + name + " не найден!"));

        List<Task> tasks = taskRepository.findTasksByAuthor(user);

        if (tasks.size() == 0) {
            throw new TaskNotFoundException("Список задач, исполнителя " + name + ", пуст!");
        }

        return tasks;
    }

    public List<Task> searchByExecutor(String name) throws TaskNotFoundException, UserNotFoundException {

        User user = userRepository.findByName(name).orElseThrow(() -> new UserNotFoundException("Пользователь с именем:" + name + " не найден!"));

        List<Task> tasks = taskRepository.findTasksByExecutor(user);

        if (tasks.size() == 0) {
            throw new TaskNotFoundException("Список задач, автора " + name + ", пуст!");
        }

        return tasks;
    }

    public Task changeStatusTaskByExecutor(Long task_id, TaskStatus status, User user) throws TaskNotFoundException, NoAccessException, UserNotFoundException {
        Task task = taskRepository.findById(task_id)
                .orElseThrow(() -> new TaskNotFoundException("Задача с ID:" + task_id + " не найдена!"));

        if (!task.getExecutor().getEmail().equals(user.getEmail())) {
            throw new NoAccessException("Нет доступа! Вы не являетесь исполнителем данной задачи!");
        }

        task.setStatus(status);

        return taskRepository.save(task);
    }

    public List<CommentDto> leaveComment(Long task_id, String content, User user) throws TaskNotFoundException {

        Task task = taskRepository.findById(task_id)
                .orElseThrow(() -> new TaskNotFoundException("Задача с ID:" + task_id + " не найдена!"));

        Comment comment = new Comment();
        comment.setTask(task);
        comment.setAuthor(user.getName());
        comment.setContent(content);
        task.addComments(comment);
        taskRepository.save(task);

        return convertCommentListToListDto(task.getComments());
    }

    private CommentDto convertCommentToDto(Comment comments) {
        return CommentDto.builder()
                .author(comments.getAuthor())
                .content(comments.getContent())
                .build();
    }

    private List<CommentDto> convertCommentListToListDto(List<Comment> comments){
        List<CommentDto> convertList = new ArrayList<>();
        for (Comment c:comments) {
            convertList.add(convertCommentToDto(c));
        }
        return convertList;
    }
}
