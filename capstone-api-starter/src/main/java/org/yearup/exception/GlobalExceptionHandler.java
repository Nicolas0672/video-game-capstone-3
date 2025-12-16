package org.yearup.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex){
        Map<String, String> error = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(e -> {
            error.put(e.getField(), e.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ShoppingCartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFoundException(ShoppingCartNotFoundException ex){
        Map<String, String> error = new HashMap<>();

        error.put("message", "Cart does not exist");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ProductNotFoundInCartException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFoundException(ProductNotFoundInCartException ex){
        Map<String, String> error = new HashMap<>();

        error.put("message", "Product does not exist in cart");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(EmailAlreadyExitsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExists(EmailAlreadyExitsException ex){
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Email already exists");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(InvalidSearchCriteraException.class)
    public ResponseEntity<Map<String, String>> handleInvalidSearchCriteria(InvalidSearchCriteraException ex){
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Need one or more search filters");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound(ProductNotFoundException ex){
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Product not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound(CategoryNotFoundException ex){
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Category not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound(ProfileNotFoundException ex){
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Profile not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }
}
