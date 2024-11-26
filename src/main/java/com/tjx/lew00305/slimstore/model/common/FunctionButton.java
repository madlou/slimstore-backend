package com.tjx.lew00305.slimstore.model.common;

import org.springframework.web.context.annotation.RequestScope;

import lombok.Data;

@Data
@RequestScope
public class FunctionButton {
    
    private int position = 0;
    private String label = "";
    private String action = "";
    private String process = "";
    private String condition = "";
    private Boolean disable = false;

}
