package com.tjx.lew00305.slimstore.register.view;

import org.springframework.web.context.annotation.RequestScope;

import com.tjx.lew00305.slimstore.register.form.Form;

import lombok.Data;

@Data
@RequestScope
public class ViewFunctionButton {

    private int position = 0;
    private String condition = "";
    private String label = "";
    private Boolean disable = false;
    private Boolean primaryFormSubmit = false;
    private Form form = new Form();

}
