package com.tjx.lew00305.slimstore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BasketLine {
    
    private String code;
    private String name;
    private String type;
    private int quantity;
    private float unitValue;

}
