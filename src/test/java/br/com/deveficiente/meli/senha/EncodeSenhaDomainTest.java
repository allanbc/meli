package br.com.deveficiente.meli.senha;

import br.com.deveficiente.meli.cadastrousuario.EncodeSenhaDomain;
import br.com.deveficiente.meli.cadastrousuario.SenhaBrutaVO;
import br.com.deveficiente.meli.cadastrousuario.SenhaEncodadaVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class EncodeSenhaDomainTest {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final EncodeSenhaDomain encodeSenhaDomain = new EncodeSenhaDomain(passwordEncoder);

    @ParameterizedTest
    @DisplayName("Deve encodar uma senha bruta e verificar se a senha encodada é válida")
    @CsvSource({
            "senhaValida123",
            "outraSenhaValida456",
            "senhaComCaracteresEspeciais!@#",
            "senhaComEspacos no meio",
            "senhaMuitoLonga1234567890123456789012345678901234567890"
    })
    void deveEncodarSenhaBrutaEVerificarValidade(String senhaBruta) {
        SenhaBrutaVO senhaBrutaVO = new SenhaBrutaVO(senhaBruta);
        SenhaEncodadaVO senhaEncodadaVO = encodeSenhaDomain.encode(senhaBrutaVO);

        // Verificar se a senha encodada é válida para a senha bruta
        //boolean isValid = passwordEncoder.matches(senhaBruta, senhaEncodadaVO.senha());
        //assert(isValid);

        assertNotNull(senhaEncodadaVO);
        assertNotNull(senhaEncodadaVO.senha());
        assertNotEquals(senhaBruta, senhaEncodadaVO.senha());
        assertTrue(passwordEncoder.matches(senhaBruta, senhaEncodadaVO.senha()));
    }

    @ParameterizedTest
    @DisplayName("Senhas iguais devem gerar hashes diferentes com BCrypt")
    @CsvSource({
            "123456",
            "senha123",
            "abcDEF123"
    })
    void senhasIguaisDevemGerarHashesDiferentes(String senhaBruta) {

        SenhaBrutaVO senhaBrutaVO = SenhaBrutaVO.from(senhaBruta);

        SenhaEncodadaVO senhaEncodadaVO1 = encodeSenhaDomain.encode(senhaBrutaVO);
        SenhaEncodadaVO senhaEncodadaVO2 = encodeSenhaDomain.encode(senhaBrutaVO);

        assertNotEquals(senhaEncodadaVO1.senha(), senhaEncodadaVO2.senha());
        assertTrue(passwordEncoder.matches(senhaBruta, senhaEncodadaVO1.senha()));
        assertTrue(passwordEncoder.matches(senhaBruta, senhaEncodadaVO2.senha()));
    }
}
