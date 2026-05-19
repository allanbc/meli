package br.com.deveficiente.meli.cadastrousuario;

import org.springframework.util.Assert;

public record SenhaEncodadaVO(String senha) {
    public SenhaEncodadaVO {
        Assert.hasText(senha, "Senha nao pode ser vazia");
    }
}