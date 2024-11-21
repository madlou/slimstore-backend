package com.tjx.lew00305.slimstore.model.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class StoreRegister implements Serializable {

    @Id
    @GeneratedValue()
    private Integer id;
    @JoinColumn(name = "store.id")
    private Integer storeId;
    private Integer number;
    private String status;
    private Integer lastTxnNumber;
    
}
