package com.tjx.lew00305.slimstore.register;

import java.sql.Timestamp;

import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisSessionRepository;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.register.Register.RegisterStatus;
import com.tjx.lew00305.slimstore.register.form.Form;
import com.tjx.lew00305.slimstore.store.Store;
import com.tjx.lew00305.slimstore.store.StoreService;
import com.tjx.lew00305.slimstore.translation.TranslationService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final StoreService storeService;
    private final RegisterRepository registerRepository;
    private final TranslationService translationService;
    private final HttpServletRequest httpServletRequest;
    private final RedisSessionRepository redisRepo;

    private final Register register;
    
    private Register addRegister(
        Integer registerNumber
    ) {
        Register storeRegister = new Register();
        storeRegister.setStore(storeService.getStoreReference());
        storeRegister.setNumber(registerNumber);
        storeRegister.setStatus(RegisterStatus.CLOSED);
        storeRegister.setLastTxnNumber(0);
        storeRegister = registerRepository.save(storeRegister);
        return storeRegister;
    }
    
    public Register getRegister() {
        return register;
    }

    public Register getRegister(
        Integer storeNumber,
        Integer registerNumber
    ) {
        Store store = storeService.getStore(storeNumber);
        Register dbRegister = registerRepository.findByStoreAndNumber(store, registerNumber);
        return dbRegister;
    }

    public Register getRegister(
        Store store,
        Integer registerNumber
    ) {
        Register dbRegister = registerRepository.findByStoreAndNumber(store, registerNumber);
        return dbRegister;
    }
    
    public Register getRegisterFromDb() {
        return registerRepository.findById(register.getId()).orElse(null);
    }
    
    public Register getRegisterReference() {
        return registerRepository.getReferenceById(register.getId());
    }

    public Session getSessionByRegister(
        Integer storeNumber,
        Integer registerNumber
    ) {
        Store store = storeService.getStore(storeNumber);
        if ((store == null) ||
            store.isSet()) {
            return null;
        }
        Register dbRegister = registerRepository.findByStoreAndNumber(store, registerNumber);
        return redisRepo.findById(dbRegister.getSessionId());
    }

    public void initialiseRegister(
        String storeRegCookie
    ) throws Exception {
        Store store = storeService.getStore();
        if (!store.isSet()) {
            if (storeRegCookie != null) {
                String[] storeRegCookieSplit = storeRegCookie.split("-");
                if (storeRegCookieSplit[1] != null) {
                    Integer storeCookie = null;
                    Integer registerCookie = null;
                    storeCookie = Integer.parseInt(storeRegCookieSplit[0]);
                    registerCookie = Integer.parseInt(storeRegCookieSplit[1]);
                    store = storeService.setStore(storeCookie);
                    setRegister(registerCookie);
                }
            }
        }
    }

    public void registerCheck() throws Exception {
        if (!getRegister().isSet()) {
            throw new RegisterChangeException(translationService.translate("error.location_enter_register"));
        }
    }

    public Register setRegister(
        Integer registerNumber
    ) throws Exception {
        Register dbRegister = getRegister(storeService.getStore().getNumber(), registerNumber);
        if (dbRegister == null) {
            return null;
        }
        String username = register.getUserName();
        updateRegisterWithClose();
        updateRegister(dbRegister);
        updateRegisterWithOpen(username);
        return dbRegister;
    }

    public void setRegisterByForm(
        Form form,
        Boolean isUserAdmin
    ) throws Exception {
        Integer registerNumber = form.getIntegerValueByKey("registerNumber");
        Register dbRegister = setRegister(registerNumber);
        if (dbRegister == null) {
            if (isUserAdmin) {
                addRegister(registerNumber);
                setRegister(registerNumber);
            } else {
                throw new RegisterChangeException(translationService.translate("error.location_invalid_store"));
            }
        }
    }
    
    public void updateRegister(
        Register register
    ) {
        this.register.setId(register.getId());
        this.register.setLastTxnNumber(register.getLastTxnNumber());
        this.register.setLastTxnTime(register.getLastTxnTime());
        this.register.setNumber(register.getNumber());
        this.register.setSessionId(register.getSessionId());
        this.register.setStatus(register.getStatus());
        this.register.setStore(register.getStore());
        this.register.setUserName(register.getUserName());
    }

    public void updateRegisterWithClose() {
        if (register.isSet()) {
            Register dbRegister = getRegister(register.getStore(), register.getNumber());
            dbRegister.setStatus(RegisterStatus.CLOSED);
            dbRegister.setUserName(null);
            dbRegister = registerRepository.save(dbRegister);
            updateRegister(dbRegister);
        }
    }
    
    public void updateRegisterWithOpen(
        String username
    ) {
        if (register.isSet()) {
            Register dbRegister = getRegister(register.getStore(), register.getNumber());
            dbRegister.setSessionId(httpServletRequest.getRequestedSessionId());
            dbRegister.setStatus(RegisterStatus.OPEN);
            dbRegister.setUserName(username);
            dbRegister = registerRepository.save(dbRegister);
            updateRegister(dbRegister);
        }
    }
    
    public Integer updateRegisterWithTransaction(
        Timestamp time
    ) {
        Integer txnNumber = 1 + register.getLastTxnNumber();
        Register dbRegister = getRegister(register.getStore(), register.getNumber());
        dbRegister.setLastTxnNumber(txnNumber);
        dbRegister.setLastTxnTime(time);
        dbRegister = registerRepository.save(dbRegister);
        updateRegister(dbRegister);
        return txnNumber;
    }
    
}
