package edu.bbte.idde.paim1949.backend.controlleradvice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.stream.Stream;

@Slf4j
@ControllerAdvice
public class ValidationErrorHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public final Stream<String> handleConstraintViolation(ConstraintViolationException e) {
        log.debug("ConstraintViolationException: ", e);
        return e.getConstraintViolations().stream()
                .map(it -> it.getPropertyPath().toString() + ": " + it.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public final Stream<String> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        log.debug("MethodArgumentNotValidException: ", e);
        return e.getBindingResult().getFieldErrors().stream()
                .map(it -> it.getField() + ": " + it.getDefaultMessage());
    }

}
