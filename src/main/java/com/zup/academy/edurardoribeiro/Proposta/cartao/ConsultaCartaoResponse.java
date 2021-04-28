package com.zup.academy.edurardoribeiro.Proposta.cartao;

import com.zup.academy.edurardoribeiro.Proposta.criacao.Proposta;
import org.springframework.util.Assert;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ConsultaCartaoResponse {

    @NotBlank
    private String id;

    @NotBlank
    private String titular;

    @NotNull
    private LocalDateTime emitidoEm;

    @NotBlank
    private String idProposta;

    public ConsultaCartaoResponse(@NotBlank String id,
                                  @NotBlank String titular,
                                  @NotNull LocalDateTime emitidoEm,
                                  @NotBlank String idProposta) {
        this.id = id;
        this.titular = titular;
        this.emitidoEm = emitidoEm;
        this.idProposta = idProposta;
    }

    public Cartao toModel(Proposta proposta) {
        Assert.isTrue(proposta.getId().equals(Long.valueOf(this.idProposta)),
                "Proposta do cartão não bate com a proposta que você está querendo associar");
        return new Cartao(this.id, this.titular, this.emitidoEm, proposta);
    }

    public String retornaCartaoOfuscado() {
        StringBuilder builder = new StringBuilder(this.id);
        builder.replace(7, 13, "** ***");
        return builder.toString();
    }

}
