package cloud.matthews.slimstore.display;



import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import cloud.matthews.slimstore.register.Register;
import cloud.matthews.slimstore.register.RegisterCookie;
import cloud.matthews.slimstore.register.RegisterService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class DisplaySecurity {

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final RegisterService registerService;
    private final DisplaySession displaySession;

    @Pointcut("execution(public * cloud.matthews.slimstore.display.DisplayApiController.*(..))")
    private void aPointCutFromDisplayApiController() {}
    
    @Around(value = "aPointCutFromDisplayApiController()")
    public Object authCheck(
        ProceedingJoinPoint joinPoint
    ) throws Throwable {
        Boolean isPublic = request.getRequestURI().substring(0, 11).equals("/api/public");
        if(isPublic || displaySession.getAuthenticated()){
            return joinPoint.proceed();
        }
        RegisterCookie storeRegisterCookie = new RegisterCookie();
        storeRegisterCookie.set(WebUtils.getCookie(request, "store-register"));
        if(!storeRegisterCookie.isValid()){
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Store-Register Cookie Missing.");
            return null;
        }
        Cookie authTokenCookie = WebUtils.getCookie(request, "token");
        if(authTokenCookie == null){
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Token Missing.");
            return null;
        }
        Register register = registerService.getRegister(storeRegisterCookie.getStore(), storeRegisterCookie.getRegister());
        String displayToken = register.getCustomerDisplayToken();
        if(displayToken == null){
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Registration Required.");
            return null;
        }
        if(!displayToken.equals(authTokenCookie.getValue())){
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Token Invalid.");
            return null;
        }
        displaySession.setStoreNumber(storeRegisterCookie.getStore());
        displaySession.setRegisterNumber(storeRegisterCookie.getRegister());
        displaySession.setAuthenticated(true);
        return joinPoint.proceed();
    }
    
}
