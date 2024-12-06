package com.tjx.lew00305.slimstore.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.model.common.Form;
import com.tjx.lew00305.slimstore.model.common.FormElement;
import com.tjx.lew00305.slimstore.model.common.FormElement.Type;
import com.tjx.lew00305.slimstore.model.common.FormElementButton;
import com.tjx.lew00305.slimstore.model.common.View.ViewName;
import com.tjx.lew00305.slimstore.model.entity.User;
import com.tjx.lew00305.slimstore.model.session.UserSession;
import com.tjx.lew00305.slimstore.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private TranslationService translationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserSession userSession;
    
    @Value("${tjx.admin.password}")
    private String adminPassword;
    
    public User getUserFromSession() {
        User user = userSession.getUser();
        if (user == null ||
            user.getCode() == null) {
            return null;
        }
        return userSession.getUser();
    }
    
    public User addUser(
        String username,
        String name,
        String email,
        String password
    ) throws Exception {
        User user = userRepository.save(new User(null, username, email, name, password));
        return user;
    }
    
    public User saveUser(
        String username,
        String name,
        String email,
        String password
    ) throws Exception {
        User user = userRepository.findByCode(username);
        user.setName(name);
        user.setEmail(email);
        if (password.length() > 0) {
            user.setPassword(password);
        }
        return userRepository.save(user);
    }
    
    public String addUserByForm(
        Form requestForm
    ) {
        try {
            addUser(requestForm.getValueByKey("code"), requestForm.getValueByKey("name"), requestForm.getValueByKey("email"), requestForm.getValueByKey(
                "password"));
            return null;
        } catch (Exception e) {
            if (e.getMessage().contains("Duplicate entry")) {
                return "Unable to create user, employee number already being used.";
            } else {
                return "Unable to create user '" + e.getMessage() + "'";
            }
        }
    }
    
    public String saveUserByForm(
        Form requestForm
    ) {
        try {
            saveUser(requestForm.getValueByKey("code"), requestForm.getValueByKey("name"), requestForm.getValueByKey("email"), requestForm.getValueByKey(
                "password"));
            return null;
        } catch (Exception e) {
            return "Unable to save user: " + e.getMessage();
        }
    }
    
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public User getUser() {
        return userSession.getUser();
    }
    
    public User getUser(
        String username
    ) {
        User user = userRepository.findByCode(username);
        if (user == null &&
            username.equals("admin")) {
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
        if (user == null ||
            user.getCode() == null) {
            return false;
        }
        return true;
    }
    
    public Boolean isLoggedOut() {
        return !isLoggedIn();
    }
    
    public Boolean isUserAdmin() {
        User user = getUser();
        if (user == null ||
            !user.getCode().equals("admin")) {
            return false;
        }
        return true;
    }
    
    public User validateLogin(
        String username,
        String password
    ) throws Exception {
        User user = getUser(username);
        if (user != null &&
            user.getPassword().equals(password)) {
            userSession.setUser(user);
            return user;
        }
        return null;
    }
    
    public User validateLoginByForm(
        Form requestForm
    ) {
        String username = requestForm.getValueByKey("code");
        String password = requestForm.getValueByKey("password");
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
        String editTranslation = translationService.translate("ui.edit");
        for (User user : users) {
            if (!user.getCode().equals("admin")) {
                FormElement editFormElement = new FormElement();
                editFormElement.setKey("code");
                editFormElement.setValue(user.getCode());
                editFormElement.setType(Type.TEXT);
                Form editForm = new Form();
                editForm.setTargetView(ViewName.USER_EDIT);
                editForm.addElement(editFormElement);
                FormElementButton button = new FormElementButton();
                button.setLabel(editTranslation);
                button.setForm(editForm);
                FormElement userRow = new FormElement();
                userRow.setType(FormElement.Type.BUTTON);
                userRow.setKey(user.getCode());
                userRow.setLabel(editTranslation);
                userRow.setValue(user.getName());
                userRow.setButton(button);
                elements.add(userRow);
            }
        }
        return elements.toArray(new FormElement[0]);
    }
    
}
