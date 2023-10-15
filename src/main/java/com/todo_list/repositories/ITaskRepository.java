package com.todo_list.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todo_list.task.Task;
import java.util.List;


public interface ITaskRepository extends JpaRepository<Task,UUID>{
    List<Task> findByIdUser(UUID idUser);
}
