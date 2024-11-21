package com.tjx.lew00305.slimstore.model.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TransactionLine {

    @Id
    @GeneratedValue()
    private Integer id;
    @JoinColumn(name = "transaction.id")
    private Integer transactionId;
    private Integer number;
    @JoinColumn(name = "product.id")
    private Integer productId;
    private String type;
    private Integer quantity;
    private Float unitValue;
    private Float lineValue;

}
