package com.zup.academy.eduardoribeiro.Proposta.compartilhado.metricas;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Metricas {

    private final String appNome;

    private MeterRegistry meterRegistry;

    public Metricas(@Value("${app.nome}") String appNome,
                    MeterRegistry meterRegistry) {
        this.appNome = appNome;
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
