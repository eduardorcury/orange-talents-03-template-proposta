package com.zup.academy.eduardoribeiro.Proposta.acompanhamento;

import com.zup.academy.eduardoribeiro.Proposta.criacao.Proposta;
import com.zup.academy.eduardoribeiro.Proposta.criacao.StatusProposta;

public class ConsultaPropostaResponse {

    private Long id;
    private StatusProposta status;

    public ConsultaPropostaResponse(Proposta proposta) {
        this.id = proposta.getId();
        this.status = proposta.getStatus();
    }

    public Long getId() {
        return id;
    }

    public StatusProposta getStatus() {
        return status;
    }
}
