package com.zup.academy.edurardoribeiro.Proposta.compartilhado.health;

import com.zup.academy.edurardoribeiro.Proposta.cartao.CartaoClient;
import feign.FeignException;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CartaoServiceHealthIndicator implements HealthIndicator {

    private final CartaoClient client;

    public CartaoServiceHealthIndicator(CartaoClient client) {
        this.client = client;
    }

    @Override
    public Health health() {
        try {
            client.healthCheck();
            return Health.up().build();
        } catch (FeignException exception) {
            return Health.down().build();
        }
    }
}
