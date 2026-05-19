package br.com.deveficiente.meli.cadastrousuario;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.Assert;

public record SenhaBrutaVO(
    String senha
) {
        public SenhaBrutaVO(@NotBlank @Length(min = 6) String senha) {
            Assert.hasLength(senha, "senha nao pode ser em branco");
            Assert.isTrue(senha.length()>=6,"senha tem que ter no mínimo 6 caracteres");
            this.senha = senha;
        }

        public static SenhaBrutaVO from(String senha) {
            return new SenhaBrutaVO(senha);
        }
}
