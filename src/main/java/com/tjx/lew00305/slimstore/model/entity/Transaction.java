package com.tjx.lew00305.slimstore.model.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
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

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "store_id")
    @JsonIgnore
    private Store store;
    @ManyToOne
    @JoinColumn(name = "register_id")
    @JsonIgnore
    private StoreRegister register;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    private Integer number;
    private Timestamp date;
    private BigDecimal total;
    @OneToMany(mappedBy = "transaction", fetch = FetchType.EAGER)
    private List<TransactionLine> lines;
    @OneToMany(mappedBy = "transaction", fetch = FetchType.EAGER)
    private List<TransactionTender> tenders;

    public BigDecimal getLineTotal() {
        BigDecimal total = BigDecimal.valueOf(0);
        for (TransactionLine line : getLines()) {
            total = total.add(line.getLineValue());
        }
        return total;
    }

    public BigDecimal getTenderTotal() {
        BigDecimal total = BigDecimal.valueOf(0);
        for (TransactionTender line : getTenders()) {
            total = total.add(line.getValue());
        }
        return total;
    }

}
