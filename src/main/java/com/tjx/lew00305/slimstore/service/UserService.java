package com.tjx.lew00305.slimstore.service;

import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.dto.UserDTO;
import com.tjx.lew00305.slimstore.entity.User;
import com.tjx.lew00305.slimstore.model.FormElement;
import com.tjx.lew00305.slimstore.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService {
    
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private UserRepository userRepository;
    
    @Value("${tjx.admin.password}")
    private String adminPassword;

    
    public UserDTO getUserFromSession() {
        return (UserDTO) request.getSession().getAttribute("user");
    }
    
    public User addUser(String username, String name, String email, String password) throws Exception {
        User user = userRepository.save(new User(null, username, email, name, password));            
        return user;
    }
    
    private User getUser(String username) throws Exception {
        User user = userRepository.findByCode(username);
        if(user == null && username.equals("admin")) {
            return addUser("admin", "Admin Person", "admin@admin.com", adminPassword);
        }
        return user;
    }
    
    public UserDTO getUserDTO(String username) throws Exception {
        return modelMapper.map(getUser(username), UserDTO.class);
    }
    
    public UserDTO validateLogin(String username, String password) throws Exception {
        User user = getUser(username);
        if(user != null && user.getPassword().equals(password)) {
            UserDTO userDto = modelMapper.map(user, UserDTO.class);
            request.getSession().setAttribute("user", userDto);
            return userDto;
        }
        return null;
    }
    
    public void logout() {
        request.getSession().removeAttribute("user");
    }

    public FormElement[] getUsersAsFormElements() {
        Iterable<User> users = userRepository.findAll();
        ArrayList<FormElement> elements = new ArrayList<FormElement>();
        for(User user: users) {
            if(!user.getCode().equals("admin")) {
                FormElement userElement = new FormElement(
                    "button", 
                    user.getCode(), 
                    "Edit", 
                    user.getName(), 
                    null, null, null
                );
                elements.add(userElement);
            }
        }
        System.out.print(elements);
        return elements.toArray(new FormElement[0]);
    }

}
