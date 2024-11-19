package com.tjx.lew00305.slimstore.model.session;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BasketLine implements Serializable {
    
    private String code;
    private String name;
    private String type;
    private int quantity;
    private float unitValue;

}
