package com.tjx.lew00305.slimstore.user;

import java.io.Serializable;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import lombok.Data;

@SuppressWarnings("serial")
@Data
@Component
@SessionScope
public class UserSession implements Serializable {

    private User user;

}
