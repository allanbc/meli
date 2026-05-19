package br.com.deveficiente.meli.usuario;

import br.com.deveficiente.meli.cadastrousuario.NovoUsuarioRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UsuarioEmailDuplicadoValidatorTest {

    private EntityManager manager;
    private TypedQuery<Long> query;

    private UsuarioEmailDuplicadoValidatorCompTest validator;

    @BeforeEach
    void setUp() {
        manager = mock(EntityManager.class);
        query = mock(TypedQuery.class);
        validator = new UsuarioEmailDuplicadoValidatorCompTest();

        ReflectionTestUtils.setField(
                validator,
                "manager",
                manager
        );
    }

    @Test
    @DisplayName("Deve aceitar email não cadastrado")
    void deveAceitarEmailNaoCadastrado() throws Exception {
        // Simula consulta retornando false (email não existe)
        // Executa validação
        // Verifica que não há erros
        NovoUsuarioRequest request =
                new NovoUsuarioRequest(
                        "allanbc@gmail.com",
                        "123000"
                );

        Errors errors =
                new BeanPropertyBindingResult(
                        request,
                        "request"
                );

        when(manager.createQuery(anyString(), eq(Long.class)))
                .thenReturn(query);
        when(query.setParameter(eq("login"), any()))
                .thenReturn(query);
        when(query.setMaxResults(1))
                .thenReturn(query);
        when(query.getResultList())
                .thenReturn(List.of(1L));
        validator.validate(request, errors);
        assertFalse(errors.hasErrors());
    }
}
