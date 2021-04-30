package com.zup.academy.eduardoribeiro.Proposta.bloqueio;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zup.academy.eduardoribeiro.Proposta.cartao.Cartao;
import com.zup.academy.eduardoribeiro.Proposta.compartilhado.validacao.IpAddress;
import org.springframework.util.Assert;

import javax.validation.constraints.NotBlank;

public class NovoBloqueioRequest {

    @IpAddress
    @NotBlank
    private String enderecoIp;

    @JsonCreator
    public NovoBloqueioRequest(@NotBlank @JsonProperty("enderecoIp") String enderecoIp) {
        this.enderecoIp = enderecoIp;
    }

    public Bloqueio toModel(String userAgent, Cartao cartao) {
        Assert.hasText(userAgent, "User Agent não informado");
        Assert.notNull(cartao, "Cartão não pode ser nulo");
        Assert.isTrue(!cartao.bloqueado(), "Cartão já está bloqueado");
        return new Bloqueio(enderecoIp, userAgent, cartao);
    }

}
