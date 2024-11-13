package com.tjx.lew00305.slimstore.model;

import lombok.Data;

@Data
public class FunctionButton {
    
    public int position;
    public String label;
    public String action;
    public String process;
    public String condition;
    public boolean disable;

}
