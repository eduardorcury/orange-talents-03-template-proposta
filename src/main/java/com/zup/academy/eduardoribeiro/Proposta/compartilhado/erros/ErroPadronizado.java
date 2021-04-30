package com.zup.academy.eduardoribeiro.Proposta.compartilhado.erros;

import java.util.Set;

public class ErroPadronizado {

    private Set<String> mensagens;

    public ErroPadronizado(Set<String> mensagens) {
        this.mensagens = mensagens;
    }

    public Set<String> getMensagens() {
        return mensagens;
    }
}
