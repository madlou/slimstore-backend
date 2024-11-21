package com.tjx.lew00305.slimstore.model.entity;

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
    private Integer id;
    @JoinColumn(name = "storeregister.id")
    private Integer registerID;
    private Integer number;
    private Timestamp date;
    private Float total;
    
}
