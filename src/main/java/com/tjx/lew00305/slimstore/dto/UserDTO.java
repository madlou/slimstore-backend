package com.tjx.lew00305.slimstore.dto;

import java.io.Serializable;

import org.springframework.web.context.annotation.SessionScope;

import lombok.Data;

@SuppressWarnings("serial")
@Data
@SessionScope
public class UserDTO implements Serializable {
    private int id;
    private String code;
    private String email;
    private String name;
}
