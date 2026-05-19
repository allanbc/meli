package br.com.deveficiente.meli.cadastrousuario;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncodeSenhaDomain {
    private final PasswordEncoder passwordEncoder;

    public EncodeSenhaDomain(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public SenhaEncodadaVO encode(SenhaBrutaVO senhaBruta) {
        return new SenhaEncodadaVO(passwordEncoder.encode(senhaBruta.senha()));
    }
}
