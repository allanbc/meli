package br.com.deveficiente.meli.senha;

import br.com.deveficiente.meli.cadastrousuario.EncodeSenhaDomain;
import br.com.deveficiente.meli.cadastrousuario.SenhaBrutaVO;
import br.com.deveficiente.meli.cadastrousuario.SenhaEncodadaVO;
import net.jqwik.api.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class EncodeSenhaDomainPropertyTest {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final EncodeSenhaDomain encodeSenhaDomain = new EncodeSenhaDomain(passwordEncoder);

    /**
     * senha bruta válida
     * EncodeSenhaDomain
     * SenhaEncodadaVO
     * Para qualquer senha bruta válida dentro das restrições aceitas pelo BCrypt,
     * o hash gerado deve bater com a senha original.
     * BCrypt → máximo de 72 bytes
     */

    @Property
    void deveEncodarQualquerSenhaBrutaValida(@ForAll("senhasBrutasValidas") String senhaBruta) {
        SenhaBrutaVO senhBrutaVO = SenhaBrutaVO.from(senhaBruta);

        SenhaEncodadaVO senhaEncodadaVO = encodeSenhaDomain.encode(senhBrutaVO);

        assertNotNull(senhaEncodadaVO);
        assertNotEquals(senhaBruta, senhaEncodadaVO.senha());
        Assert.isTrue(
                senhaBruta.getBytes(StandardCharsets.UTF_8).length <= 72,
                "Senha nao pode ter mais que 72 bytes"
        );
        assertTrue(passwordEncoder.matches(senhaBruta, senhaEncodadaVO.senha()));
     }

     @Provide
     Arbitrary<String> senhasBrutasValidas() {
            return Arbitraries.strings()
                    .withChars('a', 'b', 'c', 'd', 'e', 'f',
                            'A', 'B', 'C', 'D', 'E', 'F',
                            '1', '2', '3', '4', '5', '6',
                            '@', '#', '$', '%')
                    .ofMinLength(6)
                    .ofMaxLength(30);
     }
}
