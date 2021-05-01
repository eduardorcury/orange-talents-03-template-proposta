package com.zup.academy.eduardoribeiro.Proposta.bloqueio;

import java.util.Objects;

public class NotificacaoBloqueio {

    private final String sistemaResponsavel = "propostas";

    public String getSistemaResponsavel() {
        return sistemaResponsavel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificacaoBloqueio that = (NotificacaoBloqueio) o;
        return sistemaResponsavel.equals(that.sistemaResponsavel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sistemaResponsavel);
    }
}
