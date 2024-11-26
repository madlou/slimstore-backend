package com.tjx.lew00305.slimstore.model.session;

import java.io.Serializable;

import org.springframework.web.context.annotation.SessionScope;

import com.tjx.lew00305.slimstore.model.common.FormElement;
import com.tjx.lew00305.slimstore.model.common.FormElement.Type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
@Data
@SessionScope
public class BasketLine implements Serializable {
    
    private String code;
    private String name;
    private FormElement.Type type;
    private int quantity;
    private float unitValue;

}
