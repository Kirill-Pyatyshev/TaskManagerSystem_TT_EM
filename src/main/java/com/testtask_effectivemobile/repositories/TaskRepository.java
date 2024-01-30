package com.testtask_effectivemobile.repositories;

import com.testtask_effectivemobile.models.Task;
import com.testtask_effectivemobile.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findTasksByAuthor(User Author);
    List<Task> findTasksByExecutor(User Executor);
}