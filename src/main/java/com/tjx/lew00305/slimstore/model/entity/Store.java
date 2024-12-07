package com.tjx.lew00305.slimstore.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.annotation.SessionScope;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tjx.lew00305.slimstore.enums.Country;
import com.tjx.lew00305.slimstore.enums.Currency;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@SessionScope
public class Store implements Serializable {

    @Id
    private Integer number;
    private String name;
    @JoinColumn(name = "country.code")
    @Enumerated(EnumType.STRING)
    private Country countryCode;
    @Enumerated(EnumType.STRING)
    private Currency currencyCode;
    private String address1;
    private String address2;
    private String city;
    private String postCode;
    private String phoneNumber;
    @OneToMany(mappedBy = "store")
    @JsonIgnore
    private List<StoreRegister> registers = new ArrayList<StoreRegister>();

    public StoreRegister getRegisterByNumber(
        Integer number
    ) {
        for (StoreRegister register : registers) {
            if (register.getNumber() == number) {
                return register;
            }
        }
        return null;
    }

}
