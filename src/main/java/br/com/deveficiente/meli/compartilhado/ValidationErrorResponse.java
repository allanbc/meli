package br.com.deveficiente.meli.compartilhado;

import java.time.Instant;
import java.util.List;

public record ValidationErrorResponse(
        Instant timestamp,
        String code,
        String message,
        List<FieldErrorResponse> errors
) {
}
