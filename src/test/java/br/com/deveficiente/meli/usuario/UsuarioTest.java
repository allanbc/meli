package br.com.deveficiente.meli.usuario;

import br.com.deveficiente.meli.cadastrousuario.SenhaEncodadaVO;
import br.com.deveficiente.meli.cadastrousuario.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @ParameterizedTest
    @DisplayName("Deve criar Usuario com dados válidos")
    @CsvSource({
            "allan@email.com,$2a$10$abcdefghijklmnopqrstuv",
            "teste@email.com,$2a$10$xxxxxxxxxxxxxxxxxxxxxx"
    })
    void deveCriarUsuarioComDadosValidos(String login, String senha) {
        Usuario usuario = Usuario.create(
                login,
                new SenhaEncodadaVO(senha),
                Instant.now()
        );
        assertEquals(login, usuario.getLogin());
        assertEquals(senha, usuario.getSenha());
        assertNotNull(usuario.getInstanteCriacao());

    }

    @ParameterizedTest
    @DisplayName("Deve rejeitar login invalido")
    @CsvSource(value = {
            "NULL",
            "''",
            "' '",
            "'   '"
    }, nullValues = "NULL")
    void deveRejeitarLoginInvalido(String login) {
        try {
            Usuario.create(
                    login,
                    new SenhaEncodadaVO("$2a$10$abcdefghijklmnopqrstuv"),
                    Instant.now()
            );
        } catch (IllegalArgumentException e) {
            assertEquals("Login nao pode ser vazio", e.getMessage());
        }
    }

    @ParameterizedTest
    @DisplayName("Deve rejeitar email invalido")
    @CsvSource({
            "allan",
            "allan@",
            "@gmail.com",
            "allan@gmail",
            "allan.com",
            "allan@gmail.",
            "allan@@gmail.com",
            "allan gmail@gmail.com",
            "allan@gmail,com"
    })
    void deveRejeitarEmailInvalido(String login) {
        try {
            Usuario.create(
                    login,
                    new SenhaEncodadaVO("$2a$10$abcdefghijklmnopqrstuv"),
                    Instant.now()
            );
        } catch (IllegalArgumentException e) {
            assertEquals("Login deve ser um email válido", e.getMessage());
        }

    }

    @ParameterizedTest
    @DisplayName("Deve rejeitar instante no futuro")
    @CsvSource({
            "1",
            "5",
            "10"
    })
    void deveRejeitarInstanteNoFuturo(long segundosNoFuturo) {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Usuario.create(
                        "allan@email.com",
                        new SenhaEncodadaVO("$2a$10$abcdefghijklmnopqrstuv"),
                        Instant.now().plusSeconds(segundosNoFuturo)
                )
        );
        assertEquals("Instante nao pode estar no futuro", exception.getMessage());
    }
}
