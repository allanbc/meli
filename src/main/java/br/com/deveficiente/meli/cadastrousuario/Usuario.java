package br.com.deveficiente.meli.cadastrousuario;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.Instant;

@Entity
@Getter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private final @NotBlank String login;
    private final @NotBlank @Length(min = 6) String senha;

    @PastOrPresent
    private final Instant instanteCriacao;

    public Usuario(String login, String senha, Instant instanteCriacao) {

        Assert.hasText(login, "Login nao pode ser vazio");

        Assert.isTrue(
                login.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"),
                "Login deve ser um email válido"
        );

        Assert.hasText(senha, "Senha nao pode ser vazia");
        Assert.notNull(instanteCriacao, "Instante nao pode ser nulo");

        Assert.isTrue(
                !instanteCriacao.isAfter(Instant.now()),
                "Instante nao pode estar no futuro"
        );

        this.login = login;
        this.senha = senha;
        this.instanteCriacao = instanteCriacao;
    }

    public Usuario(@Email @NotBlank String login,
                   @Valid @NotNull SenhaBrutaVO senhaBruta) {
        Assert.isTrue(StringUtils.hasLength(login),"login não pode ser em branco");
        Assert.notNull(senhaBruta,"o objeto do tipo senha limpa nao pode ser nulo");

        this.login = login;
        this.senha = senhaBruta.senha();
        this.instanteCriacao = Instant.now();
    }

    public static Usuario create(String login, SenhaEncodadaVO senha, Instant instanteCriacao) {
        return new Usuario(login, senha.senha(), instanteCriacao);
    }
}
