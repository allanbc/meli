package br.com.deveficiente.meli.cadastrousuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NovoUsuarioRequest (
    @NotBlank(message = "{user.login.required}")
    @Email(message = "{user.login.email}")
    String login,
    @NotBlank(message = "{user.password.required}")
    @Size(min = 6, message = "{user.password.min-size}")
    String senha
) {
}
