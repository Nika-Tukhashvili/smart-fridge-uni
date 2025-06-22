package org.example.smartfridgeuni.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.example.smartfridgeuni.model.dto.ApiResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<ApiResponseDTO<Object>> ValidExceptionHandler(MethodArgumentNotValidException ex,
                                                                        HandlerMethod handlerMethod, HttpServletRequest request) {
        log.error("Exception is {}", ex);

        String errorMessage = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .findFirst()
                .map(ObjectError::getDefaultMessage)
                .orElse("Validation failed");

        ApiResponseDTO<Object> errorDto = ApiResponseDTO.error(errorMessage);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponseDTO<Object>> ValidExceptionHandler(MethodArgumentTypeMismatchException ex,
                                                                    HandlerMethod handlerMethod, HttpServletRequest request) {
        log.error("Exception is {}", ex);
        ApiResponseDTO<Object> errorDto = ApiResponseDTO.error(ex.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Object>> otherExceptionHandler(Exception ex,
                                                                    HandlerMethod handlerMethod, HttpServletRequest request) {
        ApiResponseDTO<Object> errorDto = ApiResponseDTO.error(ex.getMessage());
        log.error("Exception is {}", ex);
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleCustomException(CustomException exception, HandlerMethod method, HttpServletRequest request) {

        ApiResponseDTO<Object> errorDto = ApiResponseDTO.error(exception.getMessage());

        log.error("Exception occurred: ", exception);
        return new ResponseEntity<>(errorDto, exception.getHttpStatus() != null ? exception.getHttpStatus() : HttpStatus.BAD_REQUEST);
    }

}
