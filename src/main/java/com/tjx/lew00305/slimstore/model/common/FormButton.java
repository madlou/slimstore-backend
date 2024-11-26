package com.tjx.lew00305.slimstore.model.common;

import org.springframework.web.context.annotation.RequestScope;

import lombok.Data;

@Data
@RequestScope
public class FormButton {
    
    public String label = "";
    public String action = "";
    public String process = "";
    public Form form = new Form();
    public boolean disable = false;

}
