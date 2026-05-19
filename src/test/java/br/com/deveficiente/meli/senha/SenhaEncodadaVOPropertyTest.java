package br.com.deveficiente.meli.senha;

import br.com.deveficiente.meli.cadastrousuario.SenhaEncodadaVO;
import net.jqwik.api.*;
import net.jqwik.api.constraints.NotEmpty;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SenhaEncodadaVOPropertyTest {

    @Property
    void deveExecutarJqwik(@ForAll String valor) {

        assertNotNull(valor);
    }

    @Property
    void deveCriarSenhaEncodadaParaQualquerTextoNaoVazio(
            @ForAll("senhasComTexto") @NotEmpty String senhaEncodada
    ) {
        SenhaEncodadaVO vo = new SenhaEncodadaVO(senhaEncodada);

        assertEquals(senhaEncodada, vo.senha());
    }

    @Provide
    Arbitrary<String> senhasComTexto() {
        return Arbitraries.strings()
                .withChars('a', 'b', 'c', 'z', '1', '2', '3', '@', '#', '$')
                .ofMinLength(1)
                .ofMaxLength(100);
    }
}
