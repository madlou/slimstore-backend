package com.tjx.lew00305.slimstore.model.common;

import org.springframework.web.context.annotation.RequestScope;

import lombok.Data;

@Data
@RequestScope
public class FunctionButton {
    
    public int position = 0;
    public String label = "";
    public String action = "";
    public String process = "";
    public String condition = "";
    public boolean disable = false;

}
