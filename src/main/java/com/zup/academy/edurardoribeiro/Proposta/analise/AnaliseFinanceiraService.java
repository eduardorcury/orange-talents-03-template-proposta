package com.zup.academy.edurardoribeiro.Proposta.analise;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.academy.edurardoribeiro.Proposta.criacao.Proposta;
import com.zup.academy.edurardoribeiro.Proposta.criacao.PropostaRepository;
import feign.FeignException;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@EnableAsync
@DependsOn("associadorDeCartoes")
public class AnaliseFinanceiraService {

    private final PropostaRepository propostaRepository;
    private final AnaliseFinanceiraClient analiseClient;
    private final CacheManager cacheManager;
    private final ObjectMapper mapper;

    public AnaliseFinanceiraService(PropostaRepository propostaRepository,
                                    AnaliseFinanceiraClient analiseClient,
                                    CacheManager cacheManager, ObjectMapper mapper) {
        this.propostaRepository = propostaRepository;
        this.analiseClient = analiseClient;
        this.cacheManager = cacheManager;
        this.mapper = mapper;
    }

    @Async
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
        } finally {
            propostaRepository.save(proposta);
        }

    }

}
