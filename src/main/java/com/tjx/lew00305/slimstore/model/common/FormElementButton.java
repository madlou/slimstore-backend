package com.tjx.lew00305.slimstore.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormElementButton {

    private String label = "";
    private Form form = new Form();

}
