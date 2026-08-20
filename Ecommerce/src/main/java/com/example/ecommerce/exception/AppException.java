package com.example.ecommerce.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends RuntimeException {
    private final HttpStatus status;

    public AppException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public static class NotFoundException extends AppException {
        public NotFoundException(String message) {
            super(message, HttpStatus.NOT_FOUND);
        }
    }

    public static class DataExistsException extends AppException {
        public DataExistsException(String message) {
            super(message, HttpStatus.CONFLICT);
        }
    }

    public static class UnauthorizedException extends AppException {
        public UnauthorizedException(String message) {
            super(message, HttpStatus.UNAUTHORIZED);
        }
    }

    public static class BadRequestException extends AppException {
        public BadRequestException(String message) {
            super(message, HttpStatus.BAD_REQUEST);
        }
    }

    public static class ForbiddenException extends AppException{
        public ForbiddenException(String message) {super(message, HttpStatus.FORBIDDEN);}
    }
}
