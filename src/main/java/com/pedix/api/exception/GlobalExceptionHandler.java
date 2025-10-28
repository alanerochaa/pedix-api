package com.pedix.api.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {

    public GlobalExceptionHandler() {
        System.out.println("🔥 GlobalExceptionHandler carregado com sucesso!");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFound(EntityNotFoundException ex) {
        return buildResponse(
                HttpStatus.NOT_FOUND,
                "Pedido não encontrado.",
                ex.getMessage()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Requisição inválida.",
                ex.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                fieldErrors.put(err.getField(), err.getDefaultMessage())
        );

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Erro de validação nos campos enviados.");
        body.put("mensagem", "Por favor, corrija os campos inválidos e tente novamente.");
        body.put("detalhes", fieldErrors);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno no servidor.",
                ex.getMessage()
        );
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String titulo, String mensagem) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", titulo);
        body.put("mensagem", mensagem);
        return new ResponseEntity<>(body, status);
    }
}
