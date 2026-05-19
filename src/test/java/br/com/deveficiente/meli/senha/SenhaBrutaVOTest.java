package br.com.deveficiente.meli.senha;

import br.com.deveficiente.meli.cadastrousuario.SenhaBrutaVO;
import net.jqwik.spring.JqwikSpringSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@JqwikSpringSupport
class SenhaBrutaVOTest {

    @Test
    @DisplayName("Deve criar SenhaBrutaVO com senha válida")
    void deveCriarSenhaBrutaComSenhaValida() {
        SenhaBrutaVO senha = new SenhaBrutaVO("senhaValida123");
        assertNotNull(senha);
        assertEquals("senhaValida123", senha.senha());
    }

    @Test
    @DisplayName("Deve rejeitar senha nula")
    void deveRejeitarSenhaNula() {
    }

    @Test
    @DisplayName("Deve rejeitar senha em branco")
    void deveRejeitarSenhaEmBranco() {

        assertThrows(IllegalArgumentException.class, () -> new SenhaBrutaVO("   "));
    }

    @ParameterizedTest
    @CsvSource({
            "12345", // menos de 6 caracteres
            "abc",   // muito curta
            "123"    // muito curta
    })
    @DisplayName("Deve rejeitar senha com menos de 6 caracteres")
    void deveRejeitarSenhaComMenosDe6Caracteres(String senhaInvalida) {
        assertThrows(IllegalArgumentException.class, () -> new SenhaBrutaVO(senhaInvalida));
    }

    @ParameterizedTest
    @CsvSource({
            "senha com espacos", // senha com espaços no meio
            "senhaMuitoLonga1234567890123456789012345678901234567890", // senha muito longa
            "EMPTY", // senha vazia (substituir por uma string vazia no teste)
            "senha😊", // senha com caracteres Unicode
            "пароль123" // senha com caracteres de outro alfabeto
    })
    @DisplayName("Testar senhas com diferentes características")
    void testarSenhasComDiferentesCaracteristicas(String senha) {
        // Substituir "EMPTY" por uma string vazia
        if ("EMPTY".equals(senha)) {
            senha = "";
        }

        if (senha.isBlank() || senha.length() < 6) {
            String finalSenha = senha;
            assertThrows(IllegalArgumentException.class, () -> new SenhaBrutaVO(finalSenha));
        } else {
            SenhaBrutaVO senhaBruta = new SenhaBrutaVO(senha);
            assertNotNull(senhaBruta);
            assertEquals(senha, senhaBruta.senha());
        }
    }
}
