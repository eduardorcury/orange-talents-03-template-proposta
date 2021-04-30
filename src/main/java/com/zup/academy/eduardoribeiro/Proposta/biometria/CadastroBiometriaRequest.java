package com.zup.academy.eduardoribeiro.Proposta.biometria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zup.academy.eduardoribeiro.Proposta.cartao.Cartao;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CadastroBiometriaRequest {

    @NotBlank
    private String fingerprint;

    @JsonCreator
    public CadastroBiometriaRequest(@JsonProperty("fingerprint") String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public Biometria toModel(@NotNull Cartao cartao) {
        return new Biometria(this.fingerprint, cartao);
    }

    public String getFingerprint() {
        return fingerprint;
    }
}
