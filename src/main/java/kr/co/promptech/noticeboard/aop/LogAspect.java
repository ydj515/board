package kr.co.promptech.noticeboard.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {

    // controller 하위 패키지 안의 파라미터가 0개 이상인 모든 메소드
    @Around("execution(* kr.co.promptech.noticeboard.controller.*.*(..))")
    public Object logging(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        log.info("========== [start ] - class[{}] / method[{}]", pjp.getSignature().getDeclaringTypeName(), pjp.getSignature().getName());
        Object result = pjp.proceed();
        log.info("========== [finish] - class[{}] / method[{}] / time[{} sec]", pjp.getSignature().getDeclaringTypeName(), pjp.getSignature().getName(), (System.currentTimeMillis() - startTime) / 1000);
        return result;
    }

}
