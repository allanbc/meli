package br.com.deveficiente.meli.compartilhado;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {
    private final MessageSource messageResource;

    public ApiExceptionHandler(MessageSource messageResource) {
        this.messageResource = messageResource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handle(MethodArgumentNotValidException exception) {
        List<FieldErrorResponse> fieldErrors = exception.getBindingResult().
                getFieldErrors()
                .stream()
                .map(fieldError -> new FieldErrorResponse(
                        fieldError.getField(),
                        messageResource.getMessage(fieldError, LocaleContextHolder.getLocale())
                ))
                .toList();
        return ResponseEntity.badRequest().body(
                new ValidationErrorResponse(Instant.now(),
                        "VALIDATION_ERROR",
                        "Existem campos inválidos na requisição",
                        fieldErrors
                )
        );
    }
}
