package com.zup.academy.eduardoribeiro.Proposta.analise;

import com.zup.academy.eduardoribeiro.Proposta.criacao.StatusProposta;

public enum ResultadoAnalise {

    COM_RESTRICAO {

        @Override
        StatusProposta retornaStatus() {
            return StatusProposta.NAO_ELEGIVEL;
        }
    },

    SEM_RESTRICAO {

        @Override
        StatusProposta retornaStatus() {
            return StatusProposta.ELEGIVEL;
        }
    };

    abstract StatusProposta retornaStatus();

}
