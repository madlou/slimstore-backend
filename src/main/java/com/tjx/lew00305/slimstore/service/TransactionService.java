package com.tjx.lew00305.slimstore.service;

import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    
    public String[] getCurrentAuditLog() {
        return new String[] {
                "980/0971 Energizer Battery",
                "1@ £2.79",
                "",
                "980/3064 Duracell Battery",
                "1@ £3.79",
                "",
                "365/0376 Play Sand",
                "1@ £4.99",
                "",
                "Items: 3",
                "Total: £11.57"
        };
    }

}
