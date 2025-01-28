package com.tjx.lew00305.slimstore.user;

import java.io.Serializable;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tjx.lew00305.slimstore.store.Store;

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

@Data
@Entity
@Component
@AllArgsConstructor
@NoArgsConstructor
@SessionScope
@JsonSerialize
@JsonDeserialize(as = User.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = As.PROPERTY)
public class User implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    @Column(unique = true)
    public String code;
    public String email;
    public String name;
    public String password;
    @Enumerated(EnumType.STRING)
    public UserRole role;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "store_id")
    public Store store;
    
    @JsonIgnore
    public Boolean isAdmin() {
        if ((role != null) &&
            role.equals(UserRole.ADMIN)) {
            return true;
        }
        return false;
    }

    @JsonIgnore
    public Boolean isManagerOrAdmin() {
        if ((role != null) &&
            (role.equals(UserRole.ADMIN) ||
                role.equals(UserRole.MANAGER))) {
            return true;
        }
        return false;
    }
    
    @JsonIgnore
    public Boolean isSet() {
        return code == null ? false : true;
    }

}
