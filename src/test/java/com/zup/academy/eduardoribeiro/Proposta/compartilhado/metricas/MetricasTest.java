package com.zup.academy.eduardoribeiro.Proposta.compartilhado.metricas;

import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class MetricasTest {

    @Autowired
    MeterRegistry meterRegistry;

    @Value("${app.nome}") String appNome;

    @Test
    void deveCriarMetricasCustomizadas() {

        assertNotNull(meterRegistry.find(appNome + ".criadas").tag("estado", "nao_elegivel").counter());
        assertNotNull(meterRegistry.find(appNome + ".criadas").tag("estado", "cartao_atrelado").counter());
        assertNotNull(meterRegistry.find(appNome + ".cartoes").tag("estado", "inconsistente").counter());

    }
}