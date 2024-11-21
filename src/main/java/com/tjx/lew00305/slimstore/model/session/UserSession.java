package com.tjx.lew00305.slimstore.model.session;

import java.io.Serializable;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.tjx.lew00305.slimstore.dto.UserDTO;

import lombok.Data;

@SuppressWarnings("serial")
@Data
@Component
@SessionScope
public class UserSession implements Serializable {

    private UserDTO userDTO;
    
}