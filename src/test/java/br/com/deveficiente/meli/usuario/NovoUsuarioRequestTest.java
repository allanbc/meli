package br.com.deveficiente.meli.usuario;

import br.com.deveficiente.meli.cadastrousuario.NovoUsuarioRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NovoUsuarioRequestTest {

    private Validator validator;

    @BeforeEach
     void setup() {
         validator = Validation.buildDefaultValidatorFactory().getValidator();
     }

    @ParameterizedTest(name = "{index} - {2}")
    @MethodSource("cenarios")
    void deveValidarNovoUsuarioRequest(
            String login,
            String senha,
            String descricao,
            boolean esperadoValido,
            String campoComErro
    ) {
            NovoUsuarioRequest request = new NovoUsuarioRequest(login, senha);
            var violations =validator.validate(request);

            if (esperadoValido) {
                assertTrue(violations.isEmpty());
                return;
            }

            assertFalse(violations.isEmpty());

            assertTrue(
                    violations.stream()
                            .anyMatch(v -> v.getPropertyPath().toString().equals(campoComErro)),
                    "Esperava erro no campo: " + campoComErro
            );
    }

    static Stream<Arguments> cenarios() {
        return Stream.of(
                Arguments.of("allan@gmail.com", "123456", "request valida", true, null),

                Arguments.of(null, "123456", "login nulo", false, "login"),
                Arguments.of("", "123456", "login vazio", false, "login"),
                Arguments.of("   ", "123456", "login em branco", false, "login"),
                Arguments.of("allan", "123456", "login sem formato de email", false, "login"),
                Arguments.of("allan@", "123456", "login incompleto", false, "login"),
                Arguments.of("@gmail.com", "123456", "login sem usuario", false, "login"),

                Arguments.of("allan@gmail.com", null, "senha nula", false, "senha"),
                Arguments.of("allan@gmail.com", "", "senha vazia", false, "senha"),
                Arguments.of("allan@gmail.com", "   ", "senha em branco", false, "senha"),
                Arguments.of("allan@gmail.com", "12345", "senha com menos de 6 caracteres", false, "senha")
        );
    }
}
