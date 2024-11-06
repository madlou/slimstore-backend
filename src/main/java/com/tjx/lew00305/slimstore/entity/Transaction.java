package com.tjx.lew00305.slimstore.entity;

import java.sql.Timestamp;

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
public class Transaction {

    @Id
    @GeneratedValue()
    private int id;
    @JoinColumn(name = "storeregister.id")
    private int registerID;
    private int number;
    private Timestamp date;
    private int txnNumber;
    
}
