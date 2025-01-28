package com.tjx.lew00305.slimstore.tender;

import java.io.Serializable;
import java.math.BigDecimal;

import com.tjx.lew00305.slimstore.transaction.TransactionTender.TenderType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TenderLine implements Serializable {

    private static final long serialVersionUID = -1657317204848951935L;

    @Enumerated(EnumType.STRING)
    private TenderType type;
    private String label;
    private BigDecimal value;
    private String reference;
    
}
