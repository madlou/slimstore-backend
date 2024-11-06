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
public class StoreRegister {

    @Id
    @GeneratedValue()
    private int id;
    @JoinColumn(name = "store.id")
    private int storeId;
    private int number;
    private String status;
    private int lastTxnNumber;
    
}
