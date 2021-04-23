package com.zup.academy.edurardoribeiro.Proposta.analise;

import com.zup.academy.edurardoribeiro.Proposta.criacao.StatusProposta;

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
