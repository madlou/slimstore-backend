package com.tjx.lew00305.slimstore.location.store;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.annotation.SessionScope;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tjx.lew00305.slimstore.location.register.Register;
import com.tjx.lew00305.slimstore.product.Currency;
import com.tjx.lew00305.slimstore.translation.Language;

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
    
    public enum Banner {
        TJMAXX,
        TKMAXX,
        MARSHALLS,
        HOMEGOODS,
        HOMESENSE,
    }

    public enum Country {
        DE,
        ES,
        FR,
        IE,
        PL,
        UK,
        US,
    }
    
    public enum Region {
        EU,
        US,
        CA,
        AU,
    }

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
    @Enumerated(EnumType.STRING)
    private Language languageCode;
    @OneToMany(mappedBy = "store")
    @JsonIgnore
    private List<Register> registers = new ArrayList<Register>();

    public Register getRegisterByNumber(
        Integer number
    ) {
        for (Register register : registers) {
            if (register.getNumber() == number) {
                return register;
            }
        }
        return null;
    }

}
