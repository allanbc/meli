package br.com.deveficiente.meli.senha;

import br.com.deveficiente.meli.cadastrousuario.SenhaEncodadaVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SenhaEncodadaVOTest {

    @ParameterizedTest
    @CsvSource({
            "senhaEncodadaValida123", // senha encodada válida
            "senhaEncodadaComEspacos ", // senha encodada com espaços no final
            " senhaEncodadaComEspacosNoInicio", // senha encodada com espaços no início
            "senhaEncodadaComEspacosNoMeio", // senha encodada com espaços no meio
            "senhaEncodadaMuitoLonga1234567890123456789012345678901234567890", // senha encodada muito longa
            "senhaEncodadaComCaracteresUnicode😊", // senha encodada com caracteres Unicode
            "пароль123", // senha encodada com caracteres de outro alfabeto,
            "'$2a$10$7EqJtq98hPqEX7fNZaFWoOHIhiY2qYzWspRzYkReevX6wF4s6hG6O'",
            "'$2b$10$abcdefghijklmnopqrstuuFzJqTQ4W6FnrWv5kEKd2pYr0fR6nOWS'",
            "'{bcrypt}$2a$10$7EqJtq98hPqEX7fNZaFWoOHIhiY2qYzWspRzYkReevX6wF4s6hG6O'"
    })
    @DisplayName("Testar criação de SenhaEncodadaVO com diferentes senhas encodadas")
    void testarCriacaoSenhaEncodadaVOComDiferentesSenhasEncodadas(String senhaEncodada) {
        SenhaEncodadaVO vo = new SenhaEncodadaVO(senhaEncodada);
        assertEquals(senhaEncodada, vo.senha());
    }

    @ParameterizedTest
    @DisplayName("Deve rejeitar senha encodada nula, vazia ou em branco")
    @CsvSource(value = {
            "NULL",
            "''",
            "' '",
            "'   '"
    }, nullValues = "NULL")
    void deveRejeitarSenhaEncodadaInvalida(String senhaInvalida) {
        try {
            new SenhaEncodadaVO(senhaInvalida);
        } catch (IllegalArgumentException e) {
            assertEquals("Senha nao pode ser vazia", e.getMessage());
        }
    }
}
