package com.tjx.lew00305.slimstore.register;

import java.sql.Timestamp;
import java.util.List;

import com.tjx.lew00305.slimstore.register.Register.RegisterStatus;
import com.tjx.lew00305.slimstore.store.Store;
import com.tjx.lew00305.slimstore.transaction.Transaction;
import com.tjx.lew00305.slimstore.user.User;

import lombok.Data;

@Data
public class RegisterDTO {
    private Integer id;
    private Store store;
    private Integer number;
    private RegisterStatus status;
    private Integer lastTxnNumber;
    private List<Transaction> transactions;
    private String sessionId;
    private User user;
    private Timestamp lastTxnTime;
}
