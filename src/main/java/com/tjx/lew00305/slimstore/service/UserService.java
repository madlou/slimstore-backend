package com.tjx.lew00305.slimstore.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.dto.RegisterRequestDTO;
import com.tjx.lew00305.slimstore.dto.RegisterResponseDTO;
import com.tjx.lew00305.slimstore.model.common.Form;
import com.tjx.lew00305.slimstore.model.common.FormButton;
import com.tjx.lew00305.slimstore.model.common.FormElement;
import com.tjx.lew00305.slimstore.model.entity.User;
import com.tjx.lew00305.slimstore.model.session.UserSession;
import com.tjx.lew00305.slimstore.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserSession userSession;
        
    @Value("${tjx.admin.password}")
    private String adminPassword;

    
    public User getUserFromSession() {
        User user = userSession.getUser();
        if(user == null || user.getCode() == null) {
            return null;
        }
        return userSession.getUser();
    }
    
    public User addUser(String username, String name, String email, String password) throws Exception {
        User user = userRepository.save(new User(null, username, email, name, password));            
        return user;
    }
    
    public User saveUser(String username, String name, String email, String password) throws Exception {
        User user = userRepository.findByCode(username);
        user.setName(name);
        user.setEmail(email);
        if(password.length() > 0) {
            user.setPassword(password);            
        }
        return userRepository.save(user);            
    }
    
    public RegisterResponseDTO addUserFromRequest(RegisterRequestDTO request, RegisterResponseDTO response) {
        Form form = request.getForm();
        try {
            addUser(
                form.getValueByKey("code"),
                form.getValueByKey("name"),
                form.getValueByKey("email"),
                form.getValueByKey("password")
            );
            return response;
        } catch (Exception e) {
            if(e.getMessage().contains("Duplicate entry")) {
                response.setError("Unable to create user: Employee number already being used.");                            
            } else {
                response.setError("Unable to create user: " +  e.getMessage());
            }
            return response;
        }
    }
    
    public RegisterResponseDTO saveUserFromRequest(RegisterRequestDTO request, RegisterResponseDTO response) {
        Form form = request.getForm();
        try {
            saveUser(
                form.getValueByKey("code"),
                form.getValueByKey("name"),
                form.getValueByKey("email"),
                form.getValueByKey("password")
            );
            return response;
        } catch (Exception e) {
            response.setError("Unable to save user: " +  e.getMessage());
            return response;
        }
    }

    
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public User getUser() {
        return userSession.getUser();
    }

    public User getUser(String username) {
        User user = userRepository.findByCode(username);
        if(user == null && username.equals("admin")) {
            try {
                return addUser("admin", "Admin Person", "admin@admin.com", adminPassword);
            } catch (Exception e) {
                // do nothing
            }
        }
        return user;
    }
    
    public Boolean isLoggedIn() {
        User user = getUser();
        if(user == null || user.getCode() == null) {
            return false;
        }
        return true;
    }
    
    public Boolean isLoggedOut() {
        return !isLoggedIn();
    }
    
    public Boolean isUserAdmin() {
        User user = getUser();
        if(user == null || !user.getCode().equals("admin")) {
            return false;
        }
        return true;
    }
        
    public User validateLogin(String username, String password) throws Exception {
        User user = getUser(username);
        if(user != null && user.getPassword().equals(password)) {
            userSession.setUser(user);
            return user;
        }
        return null;
    }
    
    public User validateLoginByRequest(RegisterRequestDTO request) {
        Form form = request.getForm();
        String username = form.getValueByKey("code");
        String password = form.getValueByKey("password");
        try {
            return validateLogin(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void logout() {
        userSession.setUser(new User());
    }

    public FormElement[] getUsersAsFormElements() {
        Iterable<User> users = userRepository.findAll();
        ArrayList<FormElement> elements = new ArrayList<FormElement>();
        for(User user: users) {
            if(!user.getCode().equals("admin")) {
                FormElement userElement = new FormElement();
                userElement.setType("button");
                userElement.setKey(user.getCode());
                userElement.setLabel("Edit");
                userElement.setValue(user.getName());
                FormButton button = new FormButton();
                button.setLabel("Edit");
                button.setAction("user-edit");
                button.setProcess("SaveUser");
                Form buttonForm = button.getForm();
                FormElement buttonElement = new FormElement();
                buttonElement.setKey("code");
                buttonElement.setValue(user.getCode());
                buttonForm.addElement(buttonElement);
                button.setForm(buttonForm);
                userElement.setButton(button);
                elements.add(userElement);
            }
        }
        return elements.toArray(new FormElement[0]);
    }

}
