package com.tjx.lew00305.slimstore.user;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.register.RegisterService;
import com.tjx.lew00305.slimstore.register.form.Form;
import com.tjx.lew00305.slimstore.register.form.FormElement;
import com.tjx.lew00305.slimstore.register.form.FormElement.FormElementType;
import com.tjx.lew00305.slimstore.register.form.FormElementButton;
import com.tjx.lew00305.slimstore.register.view.View.ViewName;
import com.tjx.lew00305.slimstore.store.StoreService;
import com.tjx.lew00305.slimstore.translation.TranslationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final StoreService storeService;
    private final RegisterService registerService;
    private final TranslationService translationService;
    private final UserRepository userRepository;

    private final User user;

    @Value("${tjx.admin.password}")
    private String adminPassword;
    @Value("${tjx.app.demo}")
    private Boolean demoMode;

    public void addUserByForm(
        Form requestForm
    ) throws Exception {
        try {
            User dbUser = new User();
            dbUser.setCode(requestForm.getValueByKey("code"));
            dbUser.setEmail(requestForm.getValueByKey("email"));
            dbUser.setName(requestForm.getValueByKey("name"));
            dbUser.setPassword(requestForm.getValueByKey("password"));
            dbUser.setRole(UserRole.valueOf(requestForm.getValueByKey("role")));
            dbUser.setStore(storeService.getStoreReference());
            userRepository.save(dbUser);
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
        return user;
    }

    public User getUser(
        String username
    ) throws Exception {
        User dbUser = userRepository.findByCode(username);
        if ((dbUser == null) &&
            username.equals("admin")) {
            dbUser = new User();
            dbUser.setCode("admin");
            dbUser.setEmail("admin@admin.com");
            dbUser.setName("Admin Person");
            dbUser.setPassword(adminPassword);
            dbUser.setRole(UserRole.ADMIN);
            dbUser.setStore(null);
            userRepository.save(dbUser);
            return dbUser;
        }
        return dbUser;
    }

    public User getUserFromDb() {
        return userRepository.findById(user.getId()).orElse(null);
    }

    public User getUserReference() {
        return userRepository.getReferenceById(user.getId());
    }

    public FormElement[] getUsersAsFormElements(
        Integer storeNumber
    ) {
        Iterable<User> users;
        if (storeNumber != null) {
            users = userRepository.findByStore(storeService.getStore(storeNumber));
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
            (user.getCode() == null) ||
            user.getCode().isEmpty()) {
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
        User tempUser = getUser(username);
        if ((tempUser != null) &&
            tempUser.getPassword().equals(password)) {
            updateUser(tempUser);
            registerService.updateRegisterWithOpen(user.getName());
            return user;
        }
        throw new UserLoginException(translationService.translate("error.security_invalid_login"));
    }

    public User loginByForm(
        Form requestForm
    ) throws Exception {
        String username = requestForm.getValueByKey("code");
        String password = requestForm.getValueByKey("password");
        return login(username, password);
    }
    
    public void logout() {
        registerService.updateRegisterWithClose();
        updateUser(new User());
    }

    public void saveUserByForm(
        Form requestForm
    ) throws Exception {
        User dbUser = userRepository.findByCode(requestForm.getValueByKey("code"));
        if (isDemoMode() &&
            (dbUser.getCode().equals("1111") ||
                dbUser.getCode().equals("2222") ||
                dbUser.getCode().equals("3333"))) {
            throw new Exception(translationService.translate("error.user_demo_edit_error"));
        }
        Integer storeNumber = requestForm.getIntegerValueByKey("store");
        dbUser.setStore(storeService.getStore(storeNumber == 0 ? null : storeNumber));
        dbUser.setName(requestForm.getValueByKey("name"));
        dbUser.setEmail(requestForm.getValueByKey("email"));
        dbUser.setRole(UserRole.valueOf(requestForm.getValueByKey("role")));
        String password = requestForm.getValueByKey("password");
        if (password.length() > 0) {
            dbUser.setPassword(password);
        }
        try {
            dbUser = userRepository.save(dbUser);
            if (user.getCode().equals(dbUser.getCode())) {
                updateUser(dbUser);
            }
        } catch (Exception e) {
            throw new Exception(translationService.translate("error.user_unable_to_save", e.getMessage()));
        }
    }

    public void updateUser(
        User user
    ) {
        this.user.setCode(user.getCode());
        this.user.setEmail(user.getEmail());
        this.user.setId(user.getId());
        this.user.setName(user.getName());
        this.user.setPassword(user.getPassword());
        this.user.setRole(user.getRole());
        this.user.setStore(user.getStore());
    }
    
}
