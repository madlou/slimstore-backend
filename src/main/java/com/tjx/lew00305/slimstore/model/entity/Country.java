package com.tjx.lew00305.slimstore.model.entity;
import jakarta.persistence.Column;
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
public class Country {
    
    @Id
    @GeneratedValue()
    private int id;
    @Column(unique = true)
    private String code;
    private String name;
    @JoinColumn(name = "currency.code")
    private String currencyCode;

}
