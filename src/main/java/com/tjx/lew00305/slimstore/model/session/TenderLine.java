package com.tjx.lew00305.slimstore.model.session;

import java.io.Serializable;

import org.springframework.web.context.annotation.SessionScope;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
@Data
@SessionScope
public class TenderLine implements Serializable {
    
    private String type;
    private String label;
    private float value;
    private String reference;

}
