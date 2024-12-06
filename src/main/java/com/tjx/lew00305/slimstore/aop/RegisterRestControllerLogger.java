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
        if (userService.isLoggedOut() &&
            (requestForm.getServerProcess() != ServerProcess.LOGIN)) {
            requestForm.setTargetView(ViewName.LOGIN);
            requestForm.setServerProcess(null);
        } else {
            Store store = locationService.getStore();
            if ((store == null) &&
                (storeRegCookie != null)) {
                String[] storeRegCookieSplit = storeRegCookie.split("-");
                locationService.setLocation(storeRegCookieSplit[0], storeRegCookieSplit[1]);
                store = locationService.getStore();
            }
            if ((store == null) &&
                (requestForm.getServerProcess() != ServerProcess.CHANGE_REGISTER)) {
                requestForm.setTargetView(ViewName.REGISTER_CHANGE);
                errorMessage = "Store and register setup required.";
            }
        }
        RegisterResponseDTO result = (RegisterResponseDTO) joinPoint.proceed(new Object[] {
            requestForm, storeRegCookie, errorMessage
        });
        return result;
    }
    
}
