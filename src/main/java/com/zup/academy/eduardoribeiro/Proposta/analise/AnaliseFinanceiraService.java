package com.zup.academy.eduardoribeiro.Proposta.analise;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.academy.eduardoribeiro.Proposta.criacao.Proposta;
import com.zup.academy.eduardoribeiro.Proposta.criacao.PropostaRepository;
import feign.FeignException;
import io.micrometer.core.instrument.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
@EnableAsync
@DependsOn({"associadorDeCartoes", "metricas"})
public class AnaliseFinanceiraService {

    private final String appNome;

    private Counter naoElegiveis;

    private final PropostaRepository propostaRepository;
    private final AnaliseFinanceiraClient analiseClient;
    private final CacheManager cacheManager;
    private final ObjectMapper mapper;
    private final MeterRegistry meterRegistry;

    public AnaliseFinanceiraService(@Value("${app.nome}") String appNome,
                                    PropostaRepository propostaRepository,
                                    AnaliseFinanceiraClient analiseClient,
                                    CacheManager cacheManager,
                                    ObjectMapper mapper,
                                    MeterRegistry meterRegistry) {
        this.appNome = appNome;
        this.propostaRepository = propostaRepository;
        this.analiseClient = analiseClient;
        this.cacheManager = cacheManager;
        this.mapper = mapper;
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    private void init() {
        naoElegiveis = this.meterRegistry
                .get(appNome + ".criadas")
                .tag("estado", "nao_elegivel")
                .counter();
    }

    //@Async
    public void analise(Proposta proposta) throws IOException {

        PedidoAnaliseFinanceira pedido = new PedidoAnaliseFinanceira(proposta);
        RespostaAnaliseFinanceira resposta;
        try {
            resposta = analiseClient.consulta(pedido);
            proposta.setStatus(resposta.retornaStatusProposta());
            cacheManager.getCache("propostasElegiveis").clear();
        } catch (FeignException.UnprocessableEntity exception) {
            resposta = mapper.readValue(exception.responseBody().get().array(), RespostaAnaliseFinanceira.class);
            proposta.setStatus(resposta.retornaStatusProposta());
            naoElegiveis.increment();
        } finally {
            propostaRepository.save(proposta);
        }

    }

}
