package com.tjx.lew00305.slimstore.model.entity;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Store implements Serializable {
    
    @Id
    private Integer number;
    private String name;
    @JoinColumn(name = "country.code")
    private String countryCode;
    @OneToMany(mappedBy = "store")
    @JsonIgnore
    private List<StoreRegister> registers;
    
    public StoreRegister getRegisterByNumber(Integer number) {
        for(StoreRegister register : registers) {
            if(register.getNumber() == number) {
                return register;
            }
        }
        return null;
    }

}
