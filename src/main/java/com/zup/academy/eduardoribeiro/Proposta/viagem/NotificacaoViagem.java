package com.zup.academy.eduardoribeiro.Proposta.viagem;

import java.time.LocalDate;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificacaoViagem that = (NotificacaoViagem) o;
        return Objects.equals(destino, that.destino) && Objects.equals(validoAte, that.validoAte);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destino, validoAte);
    }
}
