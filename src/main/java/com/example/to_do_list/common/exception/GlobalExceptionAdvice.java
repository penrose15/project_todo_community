package com.example.to_do_list.common.exception;

import com.example.to_do_list.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ErrorResponse.of(e.getBindingResult());
    }

    @ExceptionHandler
    public ResponseEntity handleBusinessLogicException(BusinessLogicException e) {
        final ErrorResponse response = ErrorResponse.of(e.getExceptionCode());

        return new ResponseEntity<>(response, HttpStatus.valueOf(e.getExceptionCode().getStatus()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse handleHttpRequestMethodNotSupportedException (HttpRequestMethodNotSupportedException e) {
        return ErrorResponse.of(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, "Required request body is missing");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException e) {
        return ErrorResponse.of(e.getConstraintViolations());
    }

    @ExceptionHandler({AuthenticationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIOException(Exception e) {
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, "다시 입력해주세요.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        log.error("# handle Exception", e);

        return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
