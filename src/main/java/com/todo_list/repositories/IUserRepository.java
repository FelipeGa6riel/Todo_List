package com.todo_list.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todo_list.user.User;

public interface IUserRepository extends JpaRepository<User,UUID> {
    
    User findByUserName(String userName);
}
