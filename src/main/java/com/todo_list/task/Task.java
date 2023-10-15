package com.todo_list.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tb_tasks")
public class Task {
    
    @Id
    @GeneratedValue(generator = "UUId")
    private UUID id;
    private String description;
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String priority;

    private UUID idUser;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public void setTitle(String title) throws Exception{

        if(title.length() > 50) {
            throw new Exception("O campo title deve conter no maximo 50 caracteres");
        }
        this.title = title;
    }
}
