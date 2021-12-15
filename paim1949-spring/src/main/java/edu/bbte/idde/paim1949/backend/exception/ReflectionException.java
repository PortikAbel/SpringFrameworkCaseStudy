package edu.bbte.idde.paim1949.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ReflectionException extends RuntimeException {
    public ReflectionException() {
        super("Error occurred");
    }
}
