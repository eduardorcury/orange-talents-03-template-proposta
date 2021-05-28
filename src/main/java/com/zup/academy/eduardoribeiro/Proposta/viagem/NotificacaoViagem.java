package com.zup.academy.eduardoribeiro.Proposta.viagem;

import java.time.LocalDate;

public class NotificacaoViagem {

    private String destino;
    private LocalDate validoAte;

    NotificacaoViagem(NovoAvisoRequest request) {
        this.destino = request.getDestino();
        this.validoAte = request.getTermino();
    }

    public String getDestino() {
        return destino;
    }

    public LocalDate getValidoAte() {
        return validoAte;
    }
}
