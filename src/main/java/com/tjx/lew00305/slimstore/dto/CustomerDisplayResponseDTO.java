package com.tjx.lew00305.slimstore.dto;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tjx.lew00305.slimstore.model.session.BasketLine;
import com.tjx.lew00305.slimstore.model.session.TenderLine;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDisplayResponseDTO {

    private Integer store;
    private Integer register;
    private ArrayList<BasketLine> basket;
    private ArrayList<TenderLine> tender;

}
