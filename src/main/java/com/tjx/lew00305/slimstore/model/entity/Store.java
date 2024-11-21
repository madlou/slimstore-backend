package com.tjx.lew00305.slimstore.model.entity;
import java.io.Serializable;

import jakarta.persistence.Column;
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
public class Store  implements Serializable {
    
    @Id
    @GeneratedValue()
    private Integer id;
    @Column(unique = true)
    private Integer number;
    private String name;
    @JoinColumn(name = "country.code")
    private String countryCode;

}
