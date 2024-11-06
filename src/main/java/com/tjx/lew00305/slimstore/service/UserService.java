package com.tjx.lew00305.slimstore.service;

import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.entity.User;

@Service
public class UserService {
    
    public User getUser() {
        User dummy = new User();
        dummy.setId(1);
        dummy.setName("Lewis Matthews");
        dummy.setEmail("lewis_matthews@tjxeurope.com");
        dummy.setPassword("1234");
        dummy.setCode("lewis");
        return dummy;
    }

}
