package com.tjx.lew00305.slimstore.store;

import java.io.Serializable;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tjx.lew00305.slimstore.translation.Language;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@Entity
@Component
@NoArgsConstructor
@AllArgsConstructor
@SessionScope
@JsonSerialize
@JsonDeserialize(as = Store.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = As.PROPERTY)
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

    public enum Currency {
        EUR,
        GBP,
        PLN,
        USD,
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

    @JsonIgnore
    public Boolean isSet() {
        return number == null ? false : true;
    }
    
}
