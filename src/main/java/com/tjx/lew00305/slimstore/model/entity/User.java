package com.tjx.lew00305.slimstore.model.entity;

import java.io.Serializable;

import org.springframework.web.context.annotation.SessionScope;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tjx.lew00305.slimstore.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@SessionScope
public class User implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String code;
    private String email;
    private String name;
    @JsonIgnore
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "store_id")
    @JsonIgnore
    private Store store;
    
    public Boolean isAdmin() {
        if ((role != null) &&
            role.equals(UserRole.ADMIN)) {
            return true;
        }
        return false;
    }

    public Boolean isManagerOrAdmin() {
        if ((role != null) &&
            (role.equals(UserRole.ADMIN) ||
                role.equals(UserRole.MANAGER))) {
            return true;
        }
        return false;
    }
    
}
