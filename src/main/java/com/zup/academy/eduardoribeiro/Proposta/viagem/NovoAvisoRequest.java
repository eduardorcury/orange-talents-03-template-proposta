package com.zup.academy.eduardoribeiro.Proposta.viagem;

import com.zup.academy.eduardoribeiro.Proposta.cartao.Cartao;
import com.zup.academy.eduardoribeiro.Proposta.compartilhado.validacao.IpAddress;
import org.springframework.util.Assert;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class NovoAvisoRequest {

    @NotBlank
    private String destino;

    @NotNull
    @Future
    private LocalDate termino;

    @NotNull
    @IpAddress
    private String ip;

    public NovoAvisoRequest(@NotBlank String destino,
                            @NotNull @Future LocalDate termino,
                            @NotNull String ip) {
        this.destino = destino;
        this.termino = termino;
        this.ip = ip;
    }

    public AvisoViagem toModel(Cartao cartao, String userAgent) {

        Assert.hasText(userAgent, "User Agent não informado");
        Assert.notNull(cartao, "Cartão não pode ser nulo");
        return new AvisoViagem(this.destino, this.termino, this.ip, userAgent, cartao);

    }

    public String getDestino() {
        return destino;
    }

    public LocalDate getTermino() {
        return termino;
    }

    public String getIp() {
        return ip;
    }
}
