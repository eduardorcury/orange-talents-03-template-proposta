package com.zup.academy.eduardoribeiro.Proposta.carteira;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zup.academy.eduardoribeiro.Proposta.cartao.Cartao;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class AssociacaoCarteiraRequest {

    @NotBlank
    @Email
    private String email;

    @JsonCreator
    public AssociacaoCarteiraRequest(@NotBlank @Email @JsonProperty("email") String email) {
        this.email = email;
    }

    public Carteira toModel(Cartao cartao) {
        return new Carteira(cartao, this.email);
    }

}
