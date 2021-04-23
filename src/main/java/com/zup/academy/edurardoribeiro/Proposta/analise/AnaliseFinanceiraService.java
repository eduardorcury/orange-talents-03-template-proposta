package com.zup.academy.edurardoribeiro.Proposta.analise;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.academy.edurardoribeiro.Proposta.criacao.Proposta;
import com.zup.academy.edurardoribeiro.Proposta.criacao.PropostaRepository;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AnaliseFinanceiraService {

    private final PropostaRepository propostaRepository;
    private final AnaliseFinanceiraClient analiseClient;
    private final ObjectMapper mapper;

    public AnaliseFinanceiraService(PropostaRepository propostaRepository,
                                    AnaliseFinanceiraClient analiseClient,
                                    ObjectMapper mapper) {
        this.propostaRepository = propostaRepository;
        this.analiseClient = analiseClient;
        this.mapper = mapper;
    }

    public void analise(Proposta proposta) throws IOException {

        PedidoAnaliseFinanceira pedido = new PedidoAnaliseFinanceira(proposta);
        RespostaAnaliseFinanceira resposta = null;
        try {
            resposta = analiseClient.consulta(pedido);
        } catch (FeignException.UnprocessableEntity exception) {
            resposta = mapper.readValue(exception.responseBody().get().array(), RespostaAnaliseFinanceira.class);
        } finally {
            proposta.setStatus(resposta.retornaStatusProposta());
            propostaRepository.save(proposta);
        }

    }

}
