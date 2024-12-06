package com.tjx.lew00305.slimstore.model.common;

import org.springframework.web.context.annotation.RequestScope;

import lombok.Data;

@Data
@RequestScope
public class FunctionButton {

    private int position = 0;
    private String condition = "";
    private String label = "";
    private Boolean disable = false;
    private Boolean primaryFormSubmit = false;
    private Form form = new Form();

}
