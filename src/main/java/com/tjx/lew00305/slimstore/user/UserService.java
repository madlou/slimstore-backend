package com.tjx.lew00305.slimstore.user;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.location.LocationService;
import com.tjx.lew00305.slimstore.register.form.Form;
import com.tjx.lew00305.slimstore.register.form.FormElement;
import com.tjx.lew00305.slimstore.register.form.FormElement.FormElementType;
import com.tjx.lew00305.slimstore.register.form.FormElementButton;
import com.tjx.lew00305.slimstore.register.view.View.ViewName;
import com.tjx.lew00305.slimstore.translation.TranslationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final LocationService locationService;
    private final TranslationService translationService;
    private final UserRepository userRepository;
    private final UserSession userSession;
    
    @Value("${tjx.admin.password}")
    private String adminPassword;
    @Value("${tjx.app.demo}")
    private Boolean demoMode;
    
    public void addUserByForm(
        Form requestForm
    ) throws Exception {
        try {
            User user = new User();
            user.setCode(requestForm.getValueByKey("code"));
            user.setEmail(requestForm.getValueByKey("email"));
            user.setName(requestForm.getValueByKey("name"));
            user.setPassword(requestForm.getValueByKey("password"));
            user.setRole(UserRole.valueOf(requestForm.getValueByKey("role")));
            user.setStore(locationService.getStore());
            userRepository.save(user);
        } catch (Exception e) {
            if (e.getMessage().contains("Duplicate entry")) {
                throw new Exception(translationService.translate("error.user_duplicate_entry"));
            } else {
                throw new Exception(translationService.translate("error.user_creation_error", e.getMessage()));
            }
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
        if ((user == null) &&
            (username != null) &&
            username.equals("admin")) {
            try {
                user = new User();
                user.setCode("admin");
                user.setEmail("admin@admin.com");
                user.setName("Admin Person");
                user.setPassword(adminPassword);
                user.setRole(UserRole.ADMIN);
                user.setStore(null);
                userRepository.save(user);
                return user;
            } catch (Exception e) {
                // do nothing
            }
        }
        return user;
    }
    
    public FormElement[] getUsersAsFormElements(
        Integer storeNumber
    ) {
        Iterable<User> users;
        if (storeNumber != null) {
            users = userRepository.findByStore(locationService.getStore(storeNumber));
        } else {
            users = userRepository.findByStore(getUser().getStore());
        }
        ArrayList<FormElement> elements = new ArrayList<FormElement>();
        String editTranslation = translationService.translate("ui.edit");
        for (User user : users) {
            if (!user.getCode().equals("admin")) {
                FormElement editFormElement = new FormElement();
                editFormElement.setKey("code");
                editFormElement.setValue(user.getCode());
                editFormElement.setType(FormElementType.TEXT);
                Form editForm = new Form();
                editForm.setTargetView(ViewName.USER_EDIT);
                editForm.addElement(editFormElement);
                FormElementButton button = new FormElementButton();
                button.setLabel(editTranslation);
                button.setForm(editForm);
                FormElement userRow = new FormElement();
                userRow.setType(FormElement.FormElementType.BUTTON);
                userRow.setKey(user.getCode());
                userRow.setLabel(editTranslation);
                userRow.setValue(user.getName());
                userRow.setButton(button);
                elements.add(userRow);
            }
        }
        return elements.toArray(new FormElement[0]);
    }
    
    private Boolean isDemoMode() {
        return demoMode == null ? false : demoMode;
    }
    
    public Boolean isLoggedIn() {
        User user = getUser();
        if ((user == null) ||
            (user.getCode() == null)) {
            return false;
        }
        return true;
    }
    
    public Boolean isLoggedOut() {
        return !isLoggedIn();
    }
    
    public Boolean isUserAdmin() {
        User user = getUser();
        if ((user != null) &&
            user.isAdmin()) {
            return true;
        }
        return false;
    }

    public Boolean isUserManagerOrAdmin() {
        User user = getUser();
        if ((user != null) &&
            user.isManagerOrAdmin()) {
            return true;
        }
        return false;
    }
    
    public User login(
        String username,
        String password
    ) throws Exception {
        User user = getUser(username);
        if ((user != null) &&
            user.getPassword().equals(password)) {
            userSession.setUser(user);
            locationService.updateRegisterWithOpen(user);
            return user;
        }
        return null;
    }
    
    public User loginByForm(
        Form requestForm
    ) {
        String username = requestForm.getValueByKey("code");
        String password = requestForm.getValueByKey("password");
        try {
            return login(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void logout() {
        locationService.updateRegisterWithClose();
        userSession.setUser(new User());
    }
    
    public void saveUserByForm(
        Form requestForm
    ) throws Exception {
        User user = userRepository.findByCode(requestForm.getValueByKey("code"));
        if (isDemoMode() &&
            (user.getCode().equals("1111") ||
                user.getCode().equals("2222") ||
                user.getCode().equals("3333"))) {
            throw new Exception(translationService.translate("error.user_demo_edit_error"));
        }
        Integer storeNumber = requestForm.getIntegerValueByKey("store");
        user.setStore(locationService.getStore(storeNumber == 0 ? null : storeNumber));
        user.setName(requestForm.getValueByKey("name"));
        user.setEmail(requestForm.getValueByKey("email"));
        user.setRole(UserRole.valueOf(requestForm.getValueByKey("role")));
        String password = requestForm.getValueByKey("password");
        if (password.length() > 0) {
            user.setPassword(password);
        }
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new Exception(translationService.translate("error.user_unable_to_save", e.getMessage()));
        }
    }

}
