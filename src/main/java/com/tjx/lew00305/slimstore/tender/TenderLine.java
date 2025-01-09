package com.tjx.lew00305.slimstore.tender;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.web.context.annotation.SessionScope;

import com.tjx.lew00305.slimstore.transaction.TransactionTender.TenderType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
@Data
@SessionScope
public class TenderLine implements Serializable {
    
    @Enumerated(EnumType.STRING)
    private TenderType type;
    private String label;
    private BigDecimal value;
    private String reference;

}
