package com.tjx.lew00305.slimstore.display;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tjx.lew00305.slimstore.basket.BasketLine;
import com.tjx.lew00305.slimstore.register.Register.RegisterStatus;
import com.tjx.lew00305.slimstore.tender.TenderLine;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DisplayResponseDTO {

    private Integer store;
    private Integer register;
    @Enumerated(EnumType.STRING)
    private RegisterStatus status;
    private ArrayList<BasketLine> basket;
    private ArrayList<TenderLine> tender;

}
