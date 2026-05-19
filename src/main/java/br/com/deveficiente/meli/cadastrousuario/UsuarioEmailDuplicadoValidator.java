package br.com.deveficiente.meli.cadastrousuario;

import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

@Component
public class UsuarioEmailDuplicadoValidator implements Validator {

    private final UsuarioRepository usuarioRepository;

    public UsuarioEmailDuplicadoValidator(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return NovoUsuarioRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, org.springframework.validation.Errors errors) {
        NovoUsuarioRequest request = (NovoUsuarioRequest) target;

        if (request.login() == null || request.login().isBlank()) {
            return;
        }

        if (usuarioRepository.existsByLogin(request.login())) {
            errors.rejectValue("login", null, "Já existe usuário com este email");
        }
    }
}
