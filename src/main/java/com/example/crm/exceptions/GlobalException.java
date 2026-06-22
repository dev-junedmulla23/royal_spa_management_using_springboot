package com.example.crm.exceptions;

import com.example.crm.exceptions.handlers.*;
import com.example.crm.payload.ApiResponse;
import com.example.crm.payload.ApiResponseUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalException {

    //This method catches any error that you did NOT handle yourself.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> HandleGenericException(Exception exception){
        return new ResponseEntity<>(ApiResponseUtil.genericException(
                "error",
                exception.getMessage(),
                null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //When validation fails using: @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException exception){
        Map<String,String> errors=new HashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(error->errors.put(error.getField(),error.getDefaultMessage()));

        return new ResponseEntity<>(ApiResponseUtil.validationException("error",
                "Validation Failed",
                errors),HttpStatus.BAD_REQUEST);
    }

    // Not Found Exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ApiResponseUtil.notFound(
                "not found",
                ex.getMessage(),
                null
        ), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(EmptyListException.class)
    public ResponseEntity<?> handleEmptyList(EmptyListException ex) {
        return new ResponseEntity<>(new ApiResponse<>(
                "success",
                ex.getMessage(),
                Collections.emptyList()
        ),HttpStatus.OK);
    }

    @ExceptionHandler(EmptyPageException.class)
    public ResponseEntity<?> handleEmptyPage(EmptyPageException ex) {
        return new ResponseEntity<>(new ApiResponse<>(
                "success",
                ex.getMessage(),
                Page.empty()
        ),HttpStatus.OK);
    }

    // Duplicate Record Exception
    @ExceptionHandler(DuplicateRecordException.class)
    public ResponseEntity<?> handleDuplicateRecord(DuplicateRecordException ex) {

        return new ResponseEntity<>(ApiResponseUtil.duplicateRecord(
                "duplicate",
                ex.getMessage(),
                null
        ), HttpStatus.CONFLICT);
    }

    // handle all RuntimeException throws by code and user.
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException exception) {

        return new ResponseEntity<>(ApiResponseUtil.runtimeException(
                "runtime error",
                exception.getMessage(),
                null), HttpStatus.BAD_REQUEST);
    }

    //handle resource in use exception (for mapped entities)
    @ExceptionHandler(ResourceInUseException.class)
    public ResponseEntity<?> handleResourceInUseException(ResourceInUseException exception){
        return new ResponseEntity<>(ApiResponseUtil.resourceInUseException(
                "conflict",
                exception.getMessage(),
                null), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException exception){
        return new ResponseEntity<>(ApiResponseUtil.resourceInUseException(
                "unauthorized",
                exception.getMessage(),
                null), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<?> handleFileSizeExceeded(FileSizeLimitExceededException ex) {

        return new ResponseEntity<>(new ApiResponse<>(
                "failed",
                "Max allowed file size is 2MB",
                null
        ), HttpStatus.BAD_REQUEST);
    }




}



