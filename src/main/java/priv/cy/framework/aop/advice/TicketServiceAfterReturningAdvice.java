package priv.cy.framework.aop.advice;

import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

public class TicketServiceAfterReturningAdvice implements AfterReturningAdvice {

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("AFTER_RETURNING：本次服务已结束....");
    }
}
