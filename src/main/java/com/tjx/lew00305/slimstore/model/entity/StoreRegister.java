package com.tjx.lew00305.slimstore.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.web.context.annotation.SessionScope;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class StoreRegister implements Serializable {

    public enum RegisterStatus {
        OPEN,
        CLOSED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "store_id")
    @JsonIgnore
    private Store store;
    private Integer number;
    @Enumerated(EnumType.STRING)
    private RegisterStatus status;
    private Integer lastTxnNumber;
    @OneToMany(mappedBy = "register", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Transaction> transactions;
    @JsonIgnore
    private String sessionId;
    @JsonIgnore
    @JoinColumn(name = "last_user_id")
    @ManyToOne()
    private User user;
    private Timestamp lastTxnTime;

}
