package br.com.deveficiente.meli.cadastrousuario;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.time.Instant;

@RestController
@RequestMapping("/v1/usuarios")
public class UsuarioController {

    @PersistenceContext
    private EntityManager manager;

    private final EncodeSenhaDomain encodeSenhaDomain;
    private final Clock clock;

    private final UsuarioEmailDuplicadoValidator emailDuplicadoValidator;

    public UsuarioController(EncodeSenhaDomain encodeSenhaDomain, Clock clock, UsuarioEmailDuplicadoValidator emailDuplicadoValidator) {
        this.encodeSenhaDomain = encodeSenhaDomain;
        this.clock = clock;
        this.emailDuplicadoValidator = emailDuplicadoValidator;
    }

    @InitBinder("novoUsuarioRequest")
    public void init(WebDataBinder binder) {
        binder.addValidators(emailDuplicadoValidator);
    }


   @Operation(
            summary = "Cadastra um novo usuário",
            description = "Cria um novo usuário no sistema com login, senha encodada e instante de cadastro.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuário criado com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos na requisição",
                            content = @Content
                    )
            }
    )
    @PostMapping
    @Transactional
    public String cadastrar(@RequestBody @Valid NovoUsuarioRequest request) {

        SenhaBrutaVO senhaBrutaVO = SenhaBrutaVO.from(request.senha());
        SenhaEncodadaVO senhaEncodadaVO = encodeSenhaDomain.encode(senhaBrutaVO);

        Usuario usuario = Usuario.create(
                request.login(),
                senhaEncodadaVO,
                Instant.now(clock)
        );
        manager.persist(usuario);
        return usuario.toString();
    }
}
