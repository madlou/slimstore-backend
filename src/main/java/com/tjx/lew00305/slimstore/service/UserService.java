package com.tjx.lew00305.slimstore.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.dto.UserDTO;
import com.tjx.lew00305.slimstore.entity.User;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService {
    
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ModelMapper modelMapper;

    
    public UserDTO getUserFromSession() {
        return (UserDTO) request.getSession().getAttribute("user");
    }
    
    private User getUser(String username) {
        User dummy = new User();
        dummy.setId(1);
        dummy.setName("Lewis Matthews");
        dummy.setEmail("lewis_matthews@tjxeurope.com");
        dummy.setPassword("1234");
        dummy.setCode("lewis");
        return dummy;
    }
    
    public UserDTO validateLogin(String username, String password) {
        User user = getUser(username);
        if(user.getPassword().equals(password)) {
            UserDTO userDto = modelMapper.map(user, UserDTO.class);
            request.getSession().setAttribute("user", userDto);
            return userDto;
        }
        return null;
    }
    
    public void logout() {
        request.getSession().removeAttribute("user");
    }

}
