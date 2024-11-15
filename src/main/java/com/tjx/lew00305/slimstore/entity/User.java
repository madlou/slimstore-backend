package com.tjx.lew00305.slimstore.entity;

import org.springframework.web.context.annotation.SessionScope;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class User {
    
    @Id
    @GeneratedValue
    private int id;
    private String code;
    @Column(unique = true)
    private String email;
    private String name;
    private String password;

}
