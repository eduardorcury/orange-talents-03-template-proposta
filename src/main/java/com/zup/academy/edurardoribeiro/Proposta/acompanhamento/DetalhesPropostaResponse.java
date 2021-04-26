package com.zup.academy.edurardoribeiro.Proposta.acompanhamento;

import com.zup.academy.edurardoribeiro.Proposta.criacao.Proposta;
import com.zup.academy.edurardoribeiro.Proposta.criacao.StatusProposta;

public class DetalhesPropostaResponse {

    private Long id;
    private StatusProposta status;

    public DetalhesPropostaResponse(Proposta proposta) {
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
