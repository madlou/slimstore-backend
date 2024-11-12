package com.tjx.lew00305.slimstore.entity;
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
    private int id;
    @JoinColumn(name = "transaction.id")
    private int transactionId;
    private int number;
    @JoinColumn(name = "product.id")
    private int productId;
    private String type;
    private int quantity;
    private float unitValue;
    private float lineValue;

}
