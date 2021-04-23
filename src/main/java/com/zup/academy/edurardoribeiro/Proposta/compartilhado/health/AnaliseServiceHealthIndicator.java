package com.zup.academy.edurardoribeiro.Proposta.compartilhado.health;

import com.zup.academy.edurardoribeiro.Proposta.analise.AnaliseFinanceiraClient;
import feign.FeignException;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class AnaliseServiceHealthIndicator implements HealthIndicator {

    private final AnaliseFinanceiraClient analiseClient;

    public AnaliseServiceHealthIndicator(AnaliseFinanceiraClient analiseClient) {
        this.analiseClient = analiseClient;
    }

    @Override
    public Health health() {

        try {
            analiseClient.healthCheck();
            return Health.up().build();
        } catch (FeignException exception) {
            return Health.down().build();
        }

    }
}
