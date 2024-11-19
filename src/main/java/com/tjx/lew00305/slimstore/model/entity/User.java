package com.tjx.lew00305.slimstore.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {
    
    @Id
    @GeneratedValue
    private Integer id;
    @Column(unique = true)
    private String code;
    private String email;
    private String name;
    private String password;

}
