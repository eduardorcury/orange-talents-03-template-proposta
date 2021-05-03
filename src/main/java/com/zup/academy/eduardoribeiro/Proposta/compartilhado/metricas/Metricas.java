package com.zup.academy.eduardoribeiro.Proposta.compartilhado.metricas;

import com.zup.academy.eduardoribeiro.Proposta.criacao.PropostaRepository;
import io.micrometer.core.instrument.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Metricas {

    private final String appNome;

    private PropostaRepository propostaRepository;
    private MeterRegistry meterRegistry;

    public Metricas(@Value("${app.nome}") String appNome,
                    PropostaRepository propostaRepository,
                    MeterRegistry meterRegistry) {
        this.appNome = appNome;
        this.propostaRepository = propostaRepository;
        this.meterRegistry = meterRegistry;
        criaCounters();
    }

    private void criaCounters() {

        Counter naoElegiveis = Counter
                .builder(appNome + ".criadas")
                .tag("estado", "nao_elegivel")
                .register(meterRegistry);

        Counter cartaoAtrelado = Counter
                .builder(appNome + ".criadas")
                .tag("estado", "cartao_atrelado")
                .register(meterRegistry);

        Counter cartaoInconsistente = Counter
                .builder(appNome + ".cartoes")
                .tag("estado", "inconsistente")
                .register(meterRegistry);

    }

}
