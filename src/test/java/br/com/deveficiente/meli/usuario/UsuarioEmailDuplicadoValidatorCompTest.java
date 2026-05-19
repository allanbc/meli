package br.com.deveficiente.meli.usuario;

import br.com.deveficiente.meli.cadastrousuario.NovoUsuarioRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

@Component
public class UsuarioEmailDuplicadoValidatorCompTest implements Validator {

    @PersistenceContext
    private EntityManager manager;

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

        boolean emailExiste = manager.createQuery("""
                        select u.id
                        from Usuario u
                        where u.login = :login
                        """, Long.class)
                .setParameter("login", request.login())
                .setMaxResults(1)
                .getResultList()
                .isEmpty();
        
        if (emailExiste) {
            errors.rejectValue(
                    "login",
                    null,
                    "Já existe usuário com este email"
            );
}
}
}
