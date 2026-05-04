package com.example.banking_system.common.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.banking_system.common.dto.ResponseDto;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResponseDto<String>> handleValidation(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.failure(ex.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseDto<String>> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(404)
                .body(ResponseDto.failure(ex.getMessage()));
    }

    @ExceptionHandler(ConflictDataException.class)
    public ResponseEntity<ResponseDto<String>> handleConflict(ConflictDataException ex) {
        return ResponseEntity.status(409)
                .body(ResponseDto.failure(ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ResponseDto<String>> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(401)
                .body(ResponseDto.failure(ex.getMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ResponseDto<String>> handleForbidden(ForbiddenException ex) {
        return ResponseEntity.status(403)
                .body(ResponseDto.failure(ex.getMessage()));
    }

    // exception of validation dependency like @Valid, @NotNull, @Size, etc.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(400)
                .body(ResponseDto.failure(errorMessage));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDto<String>> handleEnumError(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest()
                .body(ResponseDto.failure("Invalid enum value"));
    }

}
