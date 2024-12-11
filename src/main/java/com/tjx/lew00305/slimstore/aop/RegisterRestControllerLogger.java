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
import com.tjx.lew00305.slimstore.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class RegisterRestControllerLogger {
    
    private LocationService locationService;
    private UserService userService;
    
    public RegisterRestControllerLogger(
        LocationService locationService,
        UserService userService
    ) {
        this.locationService = locationService;
        this.userService = userService;
    }

    @Pointcut("execution(public * com.tjx.lew00305.slimstore.controller.RegisterController.registerQuery(..))")
    private void aPointCutFromRestController() {}

    @Before(value = "aPointCutFromRestController()")
    public void logBefore(
        JoinPoint joinPoint
    ) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        log.info(">> {}() - {}", methodName, Arrays.toString(args));
    }

    @Around(value = "aPointCutFromRestController()")
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
                errorMessage = "User not found.";
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
                    errorMessage = "User not assigned to store.";
                    requestForm.setTargetView(ViewName.LOGIN);
                    requestForm.setServerProcess(null);
                    return joinPoint.proceed(new Object[] {
                        requestForm, storeRegCookie, errorMessage
                    });
                }
                if (!user.isAdmin()) {
                    Integer userStoreNumber = userStore.getNumber();
                    Integer registerStoreNumber = locationService.getStore().getNumber();
                    if (!registerStoreNumber.equals(userStoreNumber)) {
                        errorMessage = "User doesn't belong to this store.  User Store=" + userStoreNumber + ", Register Store=" + registerStoreNumber + ".";
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
            errorMessage = "Store and register setup required.";
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
