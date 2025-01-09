package com.tjx.lew00305.slimstore.location;

import java.sql.Timestamp;

import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisSessionRepository;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.location.register.Register;
import com.tjx.lew00305.slimstore.location.register.RegisterRepository;
import com.tjx.lew00305.slimstore.location.register.Register.RegisterStatus;
import com.tjx.lew00305.slimstore.location.store.Store;
import com.tjx.lew00305.slimstore.location.store.StoreRepository;
import com.tjx.lew00305.slimstore.location.store.Store.Country;
import com.tjx.lew00305.slimstore.product.Currency;
import com.tjx.lew00305.slimstore.register.form.Form;
import com.tjx.lew00305.slimstore.translation.TranslationService;
import com.tjx.lew00305.slimstore.user.User;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationSession locationSession;
    private final StoreRepository storeRepository;
    private final RegisterRepository storeRegisterRepository;
    private final TranslationService translationService;
    private final HttpServletRequest httpServletRequest;
    private final RedisSessionRepository redisRepo;

    private Register addRegister(
        Store store,
        Integer registerNumber
    ) {
        Register storeRegister = new Register();
        storeRegister.setStore(store);
        storeRegister.setNumber(registerNumber);
        storeRegister.setStatus(RegisterStatus.CLOSED);
        storeRegister.setLastTxnNumber(0);
        storeRegister = storeRegisterRepository.save(storeRegister);
        return storeRegister;
    }

    private Store addStore(
        Integer storeNumber,
        Integer registerNumber
    ) {
        Store store = new Store();
        store.setNumber(storeNumber);
        store.setName("Unset");
        store.setCountryCode(Country.UK);
        store.setCurrencyCode(Currency.GBP);
        store = storeRepository.save(store);
        store.getRegisters().add(addRegister(store, registerNumber));
        return store;
    }

    public Session getSessionByStoreRegister(
        Integer storeNumber,
        Integer registerNumber
    ) {
        Store store = getStore(storeNumber);
        if (store == null) {
            return null;
        }
        Register register = store.getRegisterByNumber(registerNumber);
        return redisRepo.findById(register.getSessionId());
    }

    public Store getStore() {
        return locationSession.getStore();
    }

    public Store getStore(
        Integer number
    ) {
        return storeRepository.findByNumber(number);
    }

    public Register getStoreRegister() {
        return locationSession.getStoreRegister();
    }

    public Iterable<Store> getStores() {
        return storeRepository.findAll();
    }

    public void saveStoreByForm(
        Form requestForm
    ) {
        Store store = getStore();
        store.setName(requestForm.getValueByKey("name"));
        store.setCountryCode(Country.valueOf(requestForm.getValueByKey("countryCode")));
        store.setCurrencyCode(Currency.valueOf(requestForm.getValueByKey("currencyCode")));
        store.setAddress1(requestForm.getValueByKey("address1"));
        store.setAddress2(requestForm.getValueByKey("address2"));
        store.setCity(requestForm.getValueByKey("city"));
        store.setPostCode(requestForm.getValueByKey("postCode"));
        store.setPhoneNumber(requestForm.getValueByKey("phoneNumber"));
        storeRepository.save(store);
    }

    public void setLocation(
        Integer storeNumber,
        Integer registerNumber
    ) {
        Store store = storeRepository.findByNumber(storeNumber);
        Register storeRegister = store.getRegisterByNumber(registerNumber);
        setLocation(store, storeRegister);
    }

    public void setLocation(
        Store store,
        Register register
    ) {
        if ((store != null) &&
            (register != null)) {
            locationSession.setStore(store);
            locationSession.setStoreRegister(register);
        }
    }

    public void setLocation(
        String storeNumber,
        String registerNumber
    ) {
        setLocation(Integer.parseInt(storeNumber), Integer.parseInt(registerNumber));
    }

    public String setLocationByForm(
        Form requestForm,
        Boolean isAdmin
    ) {
        Integer storeNumber = requestForm.getIntegerValueByKey("storeNumber");
        Integer registerNumber = requestForm.getIntegerValueByKey("registerNumber");
        Store store = storeRepository.findByNumber(storeNumber);
        if (store == null) {
            if (isAdmin) {
                store = addStore(storeNumber, registerNumber);
            } else {
                return translationService.translate("location_invalid_store");
            }
        }
        Register storeRegister = store.getRegisterByNumber(registerNumber);
        if (storeRegister == null) {
            if (isAdmin) {
                storeRegister = addRegister(store, registerNumber);
            } else {
                return translationService.translate("location_invalid_register");
            }
        }
        User user = storeRegister.getUser();
        updateRegisterWithClose();
        setLocation(store, storeRegister);
        updateRegisterWithOpen(user);
        return null;
    }

    public void updateRegisterWithClose() {
        Register register = getStoreRegister();
        if (register != null) {
            register.setStatus(RegisterStatus.CLOSED);
            register.setUser(null);
            storeRegisterRepository.save(register);
            locationSession.setStoreRegister(register);
        }
    }
    
    public void updateRegisterWithOpen(
        User user
    ) {
        Register register = getStoreRegister();
        register.setSessionId(httpServletRequest.getRequestedSessionId());
        register.setStatus(RegisterStatus.OPEN);
        register.setUser(user);
        storeRegisterRepository.save(register);
        locationSession.setStoreRegister(register);
    }

    public Integer updateRegisterWithTransaction(
        Timestamp time
    ) {
        Register register = getStoreRegister();
        Integer txnNumber = 1 + register.getLastTxnNumber();
        register.setLastTxnNumber(txnNumber);
        register.setLastTxnTime(time);
        storeRegisterRepository.save(register);
        locationSession.setStoreRegister(register);
        return txnNumber;
    }

}
