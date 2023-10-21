package kr.co.promptech.noticeboard.exception;

import kr.co.promptech.noticeboard.domain.global.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.SocketTimeoutException;
import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    private final static String ERRORS = "errors";

    @ExceptionHandler
    public ResponseEntity<?> illegalArgumentException(IllegalArgumentException exception, WebRequest request) {
        return createProblemResponse(HttpStatus.BAD_REQUEST, "입력 값을 확인해주세요.", exception.getMessage(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        BindingResult bindingResult = exception.getBindingResult();
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            String errorMessage = fieldError != null ? fieldError.getDefaultMessage() : "입력 값을 확인해주세요.";
            return createProblemResponse(HttpStatus.BAD_REQUEST, errorMessage, "confirm input value", request);
        }
        return super.handleMethodArgumentNotValid(exception, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return createProblemResponse(HttpStatus.BAD_REQUEST, "confirm input value", exception.getMessage(), request);
    }

    @ExceptionHandler
    public ResponseEntity<?> sizeLimitExceededException(SizeLimitExceededException exception, WebRequest request) {
        String errorMessage = String.format("max : %d, your request : %d", exception.getPermittedSize(), exception.getActualSize());
        return createProblemResponse(HttpStatus.BAD_REQUEST, "file size is too large", errorMessage, request);
    }

    @ExceptionHandler({RuntimeException.class})
    protected ResponseEntity<?> badRequestException(final RuntimeException exception, WebRequest request) {
        return createProblemResponse(HttpStatus.BAD_REQUEST, "The request parameter format is out of order", exception.getMessage(), request);
    }

    @ExceptionHandler({AccessDeniedException.class})
    protected ResponseEntity<?> handleAccessDeniedException(final AccessDeniedException exception, WebRequest request) {
        return createProblemResponse(HttpStatus.UNAUTHORIZED, "unauthorized", exception.getMessage(), request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return createProblemResponse(HttpStatus.METHOD_NOT_ALLOWED, "method not allowed", exception.getMessage(), request);
    }

    @ExceptionHandler({HttpMediaTypeException.class})
    protected ResponseEntity<?> httpMediaTypeException(final HttpMediaTypeException exception, WebRequest request) {
        return createProblemResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "unsupported media type", exception.getMessage(), request);
    }

    @ExceptionHandler({SocketTimeoutException.class})
    protected ResponseEntity<?> socketTimeoutException(final SocketTimeoutException exception, WebRequest request) {
        return createProblemResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Timeout On Connection", exception.getMessage(), request);
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<?> handleAll(final Exception exception, WebRequest request) {
        return createProblemResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred", exception.getMessage(), request);
    }

    private ResponseEntity<Object> createProblemResponse(HttpStatus status, String title, String detail, WebRequest request) {
        List<ErrorDto> errors = new ArrayList<>();
        errors.add(ErrorDto.builder().point("").details(detail).build());

        ProblemDetail pb = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()), "wrong input value");
        pb.setInstance(URI.create(((ServletWebRequest) request).getRequest().getRequestURL().toString()));
        pb.setTitle(title);
        pb.setProperty(ERRORS, errors);

        return ResponseEntity.status(status).body(pb);
    }
}