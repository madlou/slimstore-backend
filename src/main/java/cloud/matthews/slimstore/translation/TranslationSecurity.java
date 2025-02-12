package cloud.matthews.slimstore.translation;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class TranslationSecurity {
    
    private final UserService userService;

    @Pointcut("execution(public * cloud.matthews.slimstore.translation.TranslationController.*(..))")
    private void aPointCutFromTranslationController() {}

    @Before(value = "aPointCutFromTranslationController()")
    public void logBefore(
        JoinPoint joinPoint
    ) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        log.info(">> {}() - {}", methodName, Arrays.toString(args));
    }

    @Around(value = "aPointCutFromTranslationController()")
    public Object validateQueryAround(
        ProceedingJoinPoint joinPoint
    ) throws Throwable {
        if (!userService.isUserAdmin()) {
            String methodName = joinPoint.getSignature().getName();
            log.info(">> {}() - Not logged in as admin user.", methodName);
            return null;
        }
        return joinPoint.proceed(joinPoint.getArgs());
    }

}
