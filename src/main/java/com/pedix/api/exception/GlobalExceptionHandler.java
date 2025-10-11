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

    // Construtor para confirmar carregamento no console
    public GlobalExceptionHandler() {
        System.out.println("🔥 GlobalExceptionHandler carregado com sucesso!");
    }

    //  Recurso não encontrado (ex: Pedido inexistente)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFound(EntityNotFoundException ex) {
        return buildResponse(
                HttpStatus.NOT_FOUND,
                "❌ Pedido não encontrado.",
                ex.getMessage()
        );
    }

    // Argumento inválido (ex: item indisponível, status inválido)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "⚠️ Requisição inválida.",
                ex.getMessage()
        );
    }

    // Erros de validação (ex: campos obrigatórios ausentes)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                fieldErrors.put(err.getField(), err.getDefaultMessage())
        );

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "⚠️ Erro de validação nos campos enviados.");
        body.put("mensagem", "Por favor, corrija os campos inválidos e tente novamente.");
        body.put("detalhes", fieldErrors);
        return ResponseEntity.badRequest().body(body);
    }

    // Erro genérico (falha inesperada)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "💥 Erro interno no servidor.",
                ex.getMessage()
        );
    }

    // Metodo auxiliar para construir respostas padronizadas
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String titulo, String mensagem) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", titulo);
        body.put("mensagem", mensagem);
        return new ResponseEntity<>(body, status);
    }
}
