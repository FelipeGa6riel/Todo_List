package com.todo_list.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todo_list.repositories.ITaskRepository;
import com.todo_list.task.Task;
import com.todo_list.utils.CopyObject;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    ITaskRepository iTaskRepository;

    @PostMapping("/")
    public ResponseEntity createdTask(@RequestBody Task task, HttpServletRequest request) {

        var idUser = request.getAttribute("idUser");
        task.setIdUser((UUID) idUser);
        /*
         * Validação
         */
        var currentDate = LocalDateTime.now();
        if (currentDate.isAfter(task.getStartAt()) || currentDate.isAfter(task.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de inicio/ data de termino deve ser maior que data atual");
        }
        /*
         * validação
         */
        if (task.getStartAt().isAfter(task.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de termino deve ser depois da data de inicio");
        }

        var newTask = iTaskRepository.save(task);
        return ResponseEntity.ok().body(newTask);
    }

    @GetMapping("/")
    public List<Task> ListTask(HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        var taskList = iTaskRepository.findByIdUser((UUID) idUser);
        return taskList;
    }

    @PutMapping("/{id}")
    public ResponseEntity updateTask(@RequestBody Task taskModel, HttpServletRequest request,@PathVariable UUID id) {
        
        var task = iTaskRepository.findById(id).orElse(null);

        if(task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrada");
        }

        var idUser = request.getAttribute("idUser");

        if(!task.getIdUser().equals(idUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario não tem permissão para alterar está tarefa");
        }
        /*
         * Copia o taskModel que chega e faz o merge com a task que vem do banco de dados;
         */
        CopyObject.copyNonNullProperties(taskModel, task);
        var taskUpdate = iTaskRepository.save(task);
        return ResponseEntity.ok().body(taskUpdate);
    }
}
