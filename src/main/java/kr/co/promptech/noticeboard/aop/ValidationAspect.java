package kr.co.promptech.noticeboard.aop;

import kr.co.promptech.noticeboard.domain.global.base.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
@Slf4j
public class ValidationAspect {

    // controller 하위 패키지 안의 파라미터가 0개 이상인 모든 메소드
    @Around("execution(* kr.co.promptech.noticeboard.controller.*.*(..))")
    public Object validate(ProceedingJoinPoint pjp) throws Throwable {

        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        String requestURI = ((ServletRequestAttributes) requestAttributes).getRequest().getRequestURI();

        log.info("[validation check... requestUri=[{}] package = [{}], method = [{}]", requestURI, pjp.getSignature().getDeclaringTypeName(), pjp.getSignature().getName());

        Object[] args = pjp.getArgs();
        for (Object arg : args) {
            if (arg instanceof BindingResult bindingResult && bindingResult.hasErrors()) {   // object type == BindingResult
                List<ErrorDto> errors = new ArrayList<>();
                for (FieldError error : bindingResult.getFieldErrors()) {
                    errors.add(
                            ErrorDto.builder()
                                    .point(error.getField())
                                    .details(error.getDefaultMessage())
                                    .build()
                    );
                }

                ProblemDetail pb = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()), "wrong input value");
                pb.setInstance(URI.create(requestURI));
                pb.setTitle(HttpStatus.BAD_REQUEST.name());
                pb.setProperty("errors", errors);

                return ResponseEntity.badRequest().body(pb);
            }
        }

        return pjp.proceed();
    }
}
