package org.example.binarytreeweb.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.example.binarytreeweb.dto.LoginUserDto;
import org.example.binarytreeweb.dto.RegisterUserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ControllerLoggingAspect.class);

    @Pointcut("execution(* org.example.binarytreeweb.controller..*(..))")
    public void controllerMethods() {
    }

    @Before("controllerMethods()")
    public void logBeforeControllerMethods(JoinPoint joinPoint) {
        logger.info("Entering method: {} with arguments: {}", joinPoint.getSignature(), sanitizeArgs(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfterReturningControllerMethods(JoinPoint joinPoint, Object result) {
        logger.info("Exiting method: {} with result: {}", joinPoint.getSignature(), sanitizeResult(result));
    }

    @AfterThrowing(pointcut = "controllerMethods()", throwing = "exception")
    public void logAfterThrowingControllerMethods(JoinPoint joinPoint, Throwable exception) {
        logger.error("Exception in method: {} with message: {}", joinPoint.getSignature(), exception.getMessage());
    }

    private Object[] sanitizeArgs(Object[] args) {
        if (args == null) {
            return null;
        }
        Object[] sanitizedArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case RegisterUserDto dto -> sanitizedArgs[i] = new RegisterUserDto(
                        dto.getEmail(),
                        dto.getPassword() == null || dto.getPassword().isEmpty() ? "null" : "****",
                        dto.getFullName()
                );
                case LoginUserDto dto -> sanitizedArgs[i] = new LoginUserDto(
                        dto.getEmail(),
                        dto.getPassword() == null || dto.getPassword().isEmpty() ? "null" : "****"
                );
                case String password when password.toLowerCase().contains("password") ->
                        sanitizedArgs[i] = password.isEmpty() ? "null" : "****"; // Mask the password or show "null" if empty
                case null, default -> sanitizedArgs[i] = args[i];
            }
        }
        return sanitizedArgs;
    }

    private Object sanitizeResult(Object result) {
        if (result instanceof String password && password.toLowerCase().contains("password")) {
            return password.isEmpty() ? "null" : "****"; // Mask the password or show "null" if empty
        }
        if (result instanceof String token && token.toLowerCase().contains("token")) {
            return token.isEmpty() ? "null" : "****"; // Mask the token or show "null" if empty
        }
        return result;
    }
}
