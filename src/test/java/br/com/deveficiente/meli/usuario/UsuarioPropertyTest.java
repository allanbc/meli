package br.com.deveficiente.meli.usuario;

import br.com.deveficiente.meli.cadastrousuario.SenhaEncodadaVO;
import br.com.deveficiente.meli.cadastrousuario.Usuario;
import net.jqwik.api.*;
import org.junit.jupiter.api.DisplayName;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioPropertyTest {

    @Property
    @DisplayName("Deve criar Usuario para qualquer email válido")
    void deveCriarUsuarioParaQualquerEmailValido(@ForAll("emailsValidos") String login) {
        // Gerar um email válido usando uma expressão regular simples
       try {
           Usuario usuario = Usuario.create(
                   login,
                   new SenhaEncodadaVO("$2a$10$abcdefghijklmnopqrstuv"),
                   Instant.now()
           );
           assertEquals(login, usuario.getLogin());
           assertEquals("$2a$10$abcdefghijklmnopqrstuv", usuario.getSenha());
           assertNotNull(usuario.getInstanteCriacao());
       } catch (IllegalArgumentException e) {
           fail("Email inválido foi rejeitado: " + login);
       }
    }

    @Provide
    Arbitrary<String> emailsValidos() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(5)
                .ofMaxLength(20)
                .map(nome -> nome + "@email.com");
    }

}
