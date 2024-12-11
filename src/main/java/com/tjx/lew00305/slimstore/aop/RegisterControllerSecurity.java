package com.tjx.lew00305.slimstore.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.tjx.lew00305.slimstore.dto.RegisterRequestDTO;
import com.tjx.lew00305.slimstore.dto.RegisterResponseDTO;
import com.tjx.lew00305.slimstore.model.common.Form.ServerProcess;
import com.tjx.lew00305.slimstore.model.common.View.ViewName;
import com.tjx.lew00305.slimstore.model.entity.Store;
import com.tjx.lew00305.slimstore.model.entity.User;
import com.tjx.lew00305.slimstore.service.LocationService;
import com.tjx.lew00305.slimstore.service.TranslationService;
import com.tjx.lew00305.slimstore.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class RegisterControllerSecurity {

    private LocationService locationService;
    private TranslationService translationService;
    private UserService userService;

    public RegisterControllerSecurity(
        LocationService locationService,
        TranslationService translationService,
        UserService userService
    ) {
        this.locationService = locationService;
        this.translationService = translationService;
        this.userService = userService;
    }
    
    @Pointcut("execution(public * com.tjx.lew00305.slimstore.controller.RegisterController.registerQuery(..))")
    private void aPointCutFromRegisterController() {}
    
    @Before(value = "aPointCutFromRegisterController()")
    public void logBefore(
        JoinPoint joinPoint
    ) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        log.info(">> {}() - {}", methodName, Arrays.toString(args));
    }
    
    @Around(value = "aPointCutFromRegisterController()")
    public Object validateQueryAround(
        ProceedingJoinPoint joinPoint
    ) throws Throwable {
        Object[] args = joinPoint.getArgs();
        RegisterRequestDTO requestForm = (RegisterRequestDTO) args[0];
        String storeRegCookie = (String) args[1];
        String errorMessage = null;
        Store store = locationService.getStore();
        if (store == null) {
            Integer storeCookie = null;
            Integer registerCookie = null;
            if (storeRegCookie != null) {
                String[] storeRegCookieSplit = storeRegCookie.split("-");
                if (storeRegCookieSplit[1] != null) {
                    storeCookie = Integer.parseInt(storeRegCookieSplit[0]);
                    registerCookie = Integer.parseInt(storeRegCookieSplit[1]);
                    locationService.setLocation(storeCookie, registerCookie);
                }
            }
        }
        if (userService.isLoggedOut()) {
            if (requestForm.getServerProcess() != ServerProcess.LOGIN) {
                requestForm.setTargetView(ViewName.LOGIN);
                requestForm.setServerProcess(null);
                return joinPoint.proceed(new Object[] {
                    requestForm, storeRegCookie, errorMessage
                });
            }
            User user = userService.getUser(requestForm.getValueByKey("code"));
            if (user == null) {
                errorMessage = translationService.translate("error.security_user_not_found");
                requestForm.setTargetView(ViewName.LOGIN);
                requestForm.setServerProcess(null);
                return joinPoint.proceed(new Object[] {
                    requestForm, storeRegCookie, errorMessage
                });
            }
            if (store != null) {
                Store userStore = user.getStore();
                if ((userStore == null) &&
                    !user.isAdmin()) {
                    errorMessage = translationService.translate("error.security_user_not_found");
                    requestForm.setTargetView(ViewName.LOGIN);
                    requestForm.setServerProcess(null);
                    return joinPoint.proceed(new Object[] {
                        requestForm, storeRegCookie, errorMessage
                    });
                }
                if (!user.isAdmin()) {
                    String usrStoreNum = userStore.getNumber().toString();
                    String regStoreNum = locationService.getStore().getNumber().toString();
                    if (!usrStoreNum.equals(regStoreNum)) {
                        errorMessage = translationService.translate("error.security_user_wrong_store", usrStoreNum, regStoreNum);
                        requestForm.setTargetView(ViewName.LOGIN);
                        requestForm.setServerProcess(null);
                        return joinPoint.proceed(new Object[] {
                            requestForm, storeRegCookie, errorMessage
                        });
                    }
                }
            }
        } else if ((store == null) &&
            (requestForm.getServerProcess() != ServerProcess.CHANGE_REGISTER)) {
            requestForm.setTargetView(ViewName.REGISTER_CHANGE);
            requestForm.setServerProcess(null);
            errorMessage = translationService.translate("error.location_setup_required");
            return joinPoint.proceed(new Object[] {
                requestForm, storeRegCookie, errorMessage
            });
        }
        RegisterResponseDTO result = (RegisterResponseDTO) joinPoint.proceed(new Object[] {
            requestForm, storeRegCookie, errorMessage
        });
        return result;
    }
    
}
