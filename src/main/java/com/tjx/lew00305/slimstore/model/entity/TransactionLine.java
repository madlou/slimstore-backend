package com.tjx.lew00305.slimstore.model.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TransactionLine {
    
    public enum TransactionLineType {
        SALE,
        RETURN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "transaction_id")
    @JsonIgnore
    private Transaction transaction;
    private Integer number;
    private String productId;
    private String productCode;
    @Enumerated(EnumType.STRING)
    private TransactionLineType type;
    private Integer quantity;
    private Float unitValue;
    private Float lineValue;
    private Integer returnedQuantity;
    @Column(name = "linked_id")
    private Integer originalTransactionLineId;

}
