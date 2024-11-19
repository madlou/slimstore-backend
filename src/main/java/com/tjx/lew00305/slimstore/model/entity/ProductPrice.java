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
public class ProductPrice {

    @Id
    @GeneratedValue()
    private int id;
    @JoinColumn(name = "product.id")
    private int productId;
    @JoinColumn(name = "country.code")
    private int countryCode;
    private int price;
    private int taxRate;

}