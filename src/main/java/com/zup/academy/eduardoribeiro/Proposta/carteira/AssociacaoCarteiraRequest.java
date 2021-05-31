package com.zup.academy.eduardoribeiro.Proposta.carteira;

import com.zup.academy.eduardoribeiro.Proposta.cartao.Cartao;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class AssociacaoCarteiraRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "PAYPAL|SAMSUNG_PAY",
            message = "Tipos de carteiras possíveis são PAYPAL ou SAMSUNG_PAY")
    private String tipo;

    public AssociacaoCarteiraRequest(@NotBlank @Email String email,
                                     @NotBlank @Pattern(regexp = "PAYPAL|SAMSUNG_PAY") String tipo) {
        this.email = email;
        this.tipo = tipo;
    }

    public Carteira toModel(Cartao cartao) {
        return new Carteira(cartao, this.email, TipoDeCarteira.valueOf(this.tipo));
    }

    public String getEmail() {
        return email;
    }

    public String getTipo() {
        return tipo;
    }
}
