package cloud.matthews.slimstore.broadcast;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;

import cloud.matthews.slimstore.basket.BasketLine;
import cloud.matthews.slimstore.register.Register;
import cloud.matthews.slimstore.register.RegisterDTO;
import cloud.matthews.slimstore.store.Store;
import cloud.matthews.slimstore.store.StoreDTO;
import cloud.matthews.slimstore.tender.TenderLine;
import cloud.matthews.slimstore.user.User;
import cloud.matthews.slimstore.user.UserDTO;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BroadcastResponseDTO {

    private StoreDTO store;
    private RegisterDTO register;
    private UserDTO user;
    private ArrayList<BasketLine> basket;
    private ArrayList<TenderLine> tender;
    // TODO: used by Display package - need to seperate out
    private String token;
    private String error;

    public void setStore(
        Store store
    ) {
        if (store == null) {
            return;
        }
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setNumber(store.getNumber());
        storeDTO.setName(store.getName());
        storeDTO.setCountryCode(store.getCountryCode());
        storeDTO.setCurrencyCode(store.getCurrencyCode());
        storeDTO.setAddress1(store.getAddress1());
        storeDTO.setAddress2(store.getAddress2());
        storeDTO.setCity(store.getCity());
        storeDTO.setPostCode(store.getPostCode());
        storeDTO.setPhoneNumber(store.getPhoneNumber());
        storeDTO.setLanguageCode(store.getLanguageCode());
        this.store = storeDTO;
    }

    public void setRegister(
        Register register
    ) {
        if (register == null) {
            return;
        }
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setId(register.getId());
        registerDTO.setNumber(register.getNumber());
        registerDTO.setStatus(register.getStatus());
        registerDTO.setLastTxnNumber(register.getLastTxnNumber());
        registerDTO.setSessionId(register.getSessionId());
        registerDTO.setLastTxnTime(register.getLastTxnTime());
        this.register = registerDTO;
    }

    public void setUser(
        User user
    ) {
        if (user == null) {
            return;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setCode(user.getCode());
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());
        userDTO.setRole(user.getRole());
        this.user = userDTO;
    }

}
