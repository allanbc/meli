package br.com.deveficiente.meli.usuario;

import br.com.deveficiente.meli.cadastrousuario.*;
import br.com.deveficiente.meli.cadastrousuario.UsuarioEmailDuplicadoValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("tests")
@Transactional
class UsuarioEmailDuplicadoValidatorIntegrationTest {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    UsuarioEmailDuplicadoValidatorIntegrationTest(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Test
    @DisplayName("Deve rejeitar email se cadastrado no banco")
    void deveRejeitarEmailJaCadastradoNoBanco() {
        Usuario usuario = Usuario.create(
                "allan@gmail.com",
                new SenhaEncodadaVO("$2a$10$abcdefghijklmnopqrstuv"),
                Instant.now()
        );

        usuarioRepository.save(usuario);

        UsuarioEmailDuplicadoValidator validator = new UsuarioEmailDuplicadoValidator(usuarioRepository);

        NovoUsuarioRequest request =
                new NovoUsuarioRequest(
                        "allan@gmail.com",
                        "123456"
                );

        Errors errors =
                new BeanPropertyBindingResult(
                        request,
                        "novoUsuarioRequest"
                );

        validator.validate(request, errors);

        assertTrue(errors.hasErrors());

        assertNotNull(errors.getFieldError("login"));

        assertEquals(
                "Já existe usuário com este email",
                errors.getFieldError("login").getDefaultMessage()
        );
    }

}
